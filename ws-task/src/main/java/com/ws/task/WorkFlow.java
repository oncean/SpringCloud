package com.ws.task;


import com.ws.task.crawler.AbstractCrawler;
import com.ws.task.snapshot.WorkFlowSnapShot;
import com.ws.task.status.WorkflowStatus;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class WorkFlow {

    private String workFlowId;
    private WorkflowStatus status;
    private List<AbstractTask> steps;

    private int currentStep = 0;

    // 记录当前执行的线程
    private TaskThread currentThread;


    public WorkFlow(AbstractCrawler...steps){
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
                AbstractTask task = steps.get(i);
                currentStep = i;
                task.start();
            }
        }
    }

    public WorkFlowSnapShot getSnapshot(){
       return new WorkFlowSnapShot(this);
    }
}
