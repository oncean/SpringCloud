package com.wangsheng.springcloud.common.thread;

import java.util.concurrent.CountDownLatch;

public class CustomThread implements Runnable{

    private int num;

    private CountDownLatch countDownLatch;

    public CustomThread(int num){
        this.num = num;
    }

    @Override
    public void run() {

        try {
            System.out.println("当前处理线程："+this.num);
        }catch (Exception e){
            System.out.println("发生异常 "+ this.num);
        }finally {
            countDownLatch.countDown();
        }
    }

    protected void setCountDownLatch(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }
}
