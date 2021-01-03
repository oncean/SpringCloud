package com.wangsheng.springcloud.common.thread;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        List<CustomThread> threads = new ArrayList<>();
        for (int i=0;i<10000;i++){
            threads.add(new CustomThread(i));
        }
        MultiThreadUtils.handle(threads);
        System.out.println("success....");
    }
}
