package com.wangsheng.springcloud.common.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class MultiThreadUtils implements MultiHandler{

    private final static int BATCH_NUM = 10;

    @Override
    public void handle(List<CustomThread> threads) throws InterruptedException {
        int total = threads.size();

        int every = total % BATCH_NUM == 0 ? total%BATCH_NUM : total% BATCH_NUM + 1 ;
        ExecutorService executorService = Executors.newFixedThreadPool(every);
        CountDownLatch c;
        for (int i = 0; i < every; i++) {
            c = new CountDownLatch(BATCH_NUM);
            for(int j = every * BATCH_NUM;j<BATCH_NUM;i++){
                if(j<total){
                    CustomThread runnable = threads.get(j);
                    runnable.setCountDownLatch(c);
                    executorService.submit(threads.get(j));
                }
            }
            c.await();
        }
    }
}
