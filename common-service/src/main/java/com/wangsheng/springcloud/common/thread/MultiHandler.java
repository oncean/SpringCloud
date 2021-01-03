package com.wangsheng.springcloud.common.thread;

import java.util.List;

public interface MultiHandler {
    void handle(List<CustomThread> threads) throws InterruptedException;
}
