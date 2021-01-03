package com.wangsheng.springcloud.common.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MultiThreadUtils{

    private final static int MAX_BLOCK_QUEUE_NUM = 100;

    private static int current = 0;

    private static ExecutorService executorService;

    private static void createPool(){
        if(executorService != null){
            return;
        }
        ThreadFactory threadFactory = new MyThreadFactory();
        RejectedExecutionHandler handler = new MyRejectHandler();
        executorService = new ThreadPoolExecutor(10,20,20,TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(MAX_BLOCK_QUEUE_NUM),threadFactory,handler);
    }

    private static class MyThreadFactory implements ThreadFactory{

        private final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r,"my-thread-" + count.getAndIncrement());
            current++;
            return thread;
        }
    }

    private static class MyRejectHandler implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //继续尝试入列
            r.run();
        }
    }


    public static void handle(List<CustomThread> threads) throws InterruptedException {
        int total = threads.size();
        if(total < 1){
            return;
        }
        createPool();
        int index=0;
        while (index<total){
            CustomThread customThread = threads.get(index);
            executorService.submit(customThread);
            index++;
        }
        executorService.shutdownNow();
        System.out.println("关闭线程池...");
    }
}
