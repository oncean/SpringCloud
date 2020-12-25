package com.wangsheng.springcloud.common.redis;

import cn.hutool.core.date.DateTime;
import lombok.SneakyThrows;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RedisTool {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";


    private static final Long RELEASE_SUCCESS = 1L;


    public static boolean tryGetDistributedLock(Jedis jedis,String lockKey,String requestId,int expireTime){
        String result = jedis.set(lockKey,requestId,SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,expireTime);
        /**
         *
         * public String set(final String key, final String value, final String nxxx, final String expx, final int time)
         * key: 锁的名称
         * value： 通过给value赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据
         * nxxx: 这个参数我们填的是NX，意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作
         * expx: 这个参数我们传的是PX，意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。
         * time: 代表key的过期时间
         */
        return LOCK_SUCCESS.equals(result);
    }


    public static boolean releaseDistributedLock(Jedis jedis,String lockKey,String requestId){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey),Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(result);
    }

    public static void lock(RedissonClient redissonClient){
        RLock lock = redissonClient.getLock("myLock");
    }

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setPassword("root")
                .setAddress("redis://127.0.0.1:6379");

        RedissonClient redissonClient = Redisson.create(config);
        //myThread1.start();
        System.out.println("success");
    }
}
