package com.wangsheng.springcloud.common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RedisTool {

    //锁名称
    public static final String LOCK_PREFIX = "redis_lock";
    //加锁失效时间，毫秒
    public static final int LOCK_EXPIRE = 10000; // ms

    private static RedisTemplate redisTemplate;

    @Autowired
    public RedisTool(RedisTemplate redisTemplate) {
        Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();
        RedisTool.redisTemplate = redisTemplate;
    }


    public static boolean lock(String id) {
        String lock_id = LOCK_PREFIX + id;
        return (Boolean) redisTemplate.execute((RedisCallback) redisConnection -> {
            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            return redisConnection.setNX(lock_id.getBytes(), String.valueOf(expireAt).getBytes());
        });
    }

    public static void unLock(String id) {
        redisTemplate.delete(id);
    }

    public static void setRedis() {
        BoundHashOperations opt = redisTemplate.boundHashOps("wangsheng-map");
        opt.put("id", "1");
        opt.put("name", "wangsheng");
    }

    public static Map getRedis() {
        BoundHashOperations opt = redisTemplate.boundHashOps("wangsheng-map");
        return opt.entries();
    }


    public static void main(String[] args) throws InterruptedException {
        Boolean aBoolean = RedisTool.lock("12");
        BoundHashOperations opt = redisTemplate.boundHashOps("wangsheng-map");
        opt.put("id", "1");
        opt.put("name", "wangsheng");
        Map re = opt.entries();

        if (aBoolean) {
            Thread.sleep(1000);
            RedisTool.unLock("12");
        } else {
            //任务已经被处理
        }
    }
}
