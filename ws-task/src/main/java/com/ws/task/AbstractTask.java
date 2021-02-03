package com.ws.task;

import com.ws.task.snapshot.TaskSnapShot;
import com.ws.task.status.TaskStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTask {

    private TaskStatus status = TaskStatus.NEW;

    public abstract void run();

    public abstract TaskSnapShot snapshot();

    public void start() {
        this.status = TaskStatus.WORKING;
        try{
            run();
        }catch (Exception e){
            log.error("crawler error ", e);
            this.status= TaskStatus.ERROR;
        }
        this.status = TaskStatus.END;
    }
}
