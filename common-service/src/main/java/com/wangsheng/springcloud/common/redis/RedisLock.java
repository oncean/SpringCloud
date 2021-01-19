package com.wangsheng.springcloud.common.redis;

public interface RedisLock {
    public boolean lock(String id) throws InterruptedException;

    public boolean unLock(String id);
}
