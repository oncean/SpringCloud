package com.wangsheng.springcloud.common.redis.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class RenewLockThread extends Thread{

    private String key;

    private RedisTemplate redisTemplate;

    private long time; // 续期时间


    @Override
    public void run(){
        while(true){
            redisTemplate.expire(key,time, TimeUnit.MILLISECONDS);
            try{
                Thread.sleep(time/2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
