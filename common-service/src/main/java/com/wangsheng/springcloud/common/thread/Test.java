package com.wangsheng.springcloud.common.thread;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Runnable> threads = new ArrayList<>();
        for (int i=0;i<100;i++){
            threads.add(new CustomThread(i));
        }
        MultiHandler multiHandler = new MultiThreadUtils();
        multiHandler.handle(threads);

    }
}
