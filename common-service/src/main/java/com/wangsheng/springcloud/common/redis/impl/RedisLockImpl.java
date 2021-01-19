package com.wangsheng.springcloud.common.redis.impl;

import com.wangsheng.springcloud.common.redis.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLockImpl implements RedisLock {

    //锁名称
    public static final String LOCK_PREFIX = "redis_lock";
    //加锁失效时间，毫秒
    public static final int LOCK_EXPIRE = 10000; // ms

    public static final long LOCK_WAIT = 10; // 自旋等待时间

    public static final int LOCK_WAIT_TIMES = 10; // 自旋尝试次数

    private RedisTemplate redisTemplate;

    private ThreadLocal<Integer> threadLockNum = new ThreadLocal();

    private ThreadLocal<Thread> renewLockThreadLocal = new ThreadLocal<>();

    @Autowired
    public RedisLockImpl(RedisTemplate redisTemplate) {
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean lock(String id) throws InterruptedException {
        String lock_id = LOCK_PREFIX + id;

        int times = 0;
        while (times< LOCK_WAIT_TIMES){
            if(tryLock(lock_id)){
                return true;
            }
            Thread.sleep(LOCK_WAIT);
            times++;
        }

        return false;
    }

    private boolean tryLock(String lockId) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        Boolean result = valueOperations.setIfAbsent(lockId,Thread.currentThread().getId(),LOCK_EXPIRE, TimeUnit.MILLISECONDS);
        if(result){
            threadLockNum.set(1);
            Thread renewLockThread = new RenewLockThread(lockId,redisTemplate,LOCK_EXPIRE/2);

            renewLockThread.start();
            renewLockThreadLocal.set(renewLockThread);
            return true;
        }
        long threadId =  valueOperations.get(lockId);
        if(threadId == Thread.currentThread().getId()){
            //可重入锁
            threadLockNum.set(threadLockNum.get()+1);
            return true;
        }
        return false;
    }

    @Override
    public boolean unLock(String id) {
        String lock_id = LOCK_PREFIX + id;
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        long threadId =  valueOperations.get(lock_id);
        if(threadId == Thread.currentThread().getId()) {
            Integer lockNum = threadLockNum.get();
            if(lockNum == null || lockNum<=1){
                redisTemplate.delete(lock_id);
                threadLockNum.remove();
                //去除守护线程
                renewLockThreadLocal.get().interrupt();
                return true;
            }
            threadLockNum.set(lockNum-1);
            return true;
        }
        return false;
    }
}
