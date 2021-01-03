package com.wangsheng.springcloud.common.thread;

import java.util.concurrent.CountDownLatch;

public class CustomThread implements Runnable{

    private int num;

    private CountDownLatch countDownLatch;

    public CustomThread(int num){
        //System.out.println("创建线程 " + num);
        this.num = num;
    }

    @Override
    public void run() {
        try {
            long time = (long) (Math.random()*1000) + 1000;
            //System.out.println("当前处理线程："+this.num);
            Thread.sleep(time);
            System.out.println(Thread.currentThread().getName() + "处理" + this.num+ "完成，耗时" + time);
        }catch (Exception e){
            System.out.println("发生异常 "+ this.num);
        }finally {
            if(countDownLatch != null){
                countDownLatch.countDown();
            }
        }
    }

    protected void setCountDownLatch(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }
}
