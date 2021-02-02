package com.ws.task;


import java.util.Arrays;
import java.util.List;

public class WorkFlow {

    private enum Status{
        NEW,WORKING,END,ERROR
    }
    private String workFlowId;
    private Status status;
    private List<AbstractCrawler> steps;

    private int currentStep;

    // 记录当前执行的线程
    private TaskThread currentThread;


    public WorkFlow(AbstractCrawler ...steps){
        this.steps = Arrays.asList(steps);
    }

    public void start(){
        if(currentThread == null){
            currentThread = new TaskThread();
            currentThread.start();
        }else{
            if(!currentThread.isAlive()){
                //如果停止 则重新启动
                currentThread = new TaskThread();
                currentThread.start();
            }
        }
    }


    public class TaskThread extends Thread{
        @Override
        public void run(){
            for (int i = 0; i < steps.size(); i++) {
                AbstractCrawler abstractCrawler = steps.get(i);
                currentStep = i;
                abstractCrawler.start();
            }
        }
    }
}
