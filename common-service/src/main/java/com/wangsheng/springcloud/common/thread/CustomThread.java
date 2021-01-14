package com.wangsheng.springcloud.common.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class CustomThread implements Runnable{

    private int num;

    private CountDownLatch countDownLatch;
    static final ThreadLocal thread = new ThreadLocal();
    ReentrantLock lock = new ReentrantLock();

    ConcurrentHashMap map = new ConcurrentHashMap(12);
    public CustomThread(int num) throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                10,
                20,
                10,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        lock.lock();
        lock.unlock();
        countDownLatch.countDown();
        countDownLatch.await();
        this.num = num;
        thread.set(1);
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


    public static void main(String[] args){
        //测试引用传递
        Map<String,String> a = new HashMap<>();
        a.put("1", "a");
        List<Map> b = new ArrayList<Map>();
        b.add(a);
        a.put("2","b");
        Map aa = b.get(0);
        System.out.println(aa.get("1"));
        //结论传递的是引用的副本
        //测试值传递
        int xx = 1;
        add(xx);
        System.out.print(xx);
        while (true){

        }
    }

    public static void add(int temp){
        temp++;

    }
}
