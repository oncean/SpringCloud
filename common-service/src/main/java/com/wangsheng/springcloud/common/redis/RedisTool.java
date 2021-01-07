package com.wangsheng.springcloud.common.redis;

import cn.hutool.core.date.DateTime;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class RedisTool {

    //锁名称
    public static final String LOCK_PREFIX = "redis_lock";
    //加锁失效时间，毫秒
    public static final int LOCK_EXPIRE = 10000; // ms

    private static RedisTemplate redisTemplate;

    @Autowired
    public RedisTool(RedisTemplate redisTemplate){
        RedisTool.redisTemplate = redisTemplate;
    }



    public static boolean  lock(String id){
        String lock_id = LOCK_PREFIX + id;
        return (Boolean) redisTemplate.execute((RedisCallback) redisConnection -> {
            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            return redisConnection.setNX(lock_id.getBytes(),String.valueOf(expireAt).getBytes());
        });
    }

    public static void unLock(String id){
        redisTemplate.delete(id);
    }



    public static void main(String[] args) throws InterruptedException {
        Boolean aBoolean = RedisTool.lock("12");
        if(aBoolean){
            Thread.sleep(1000);
            RedisTool.unLock("12");
        }else{
            //任务已经被处理
        }
    }
}
