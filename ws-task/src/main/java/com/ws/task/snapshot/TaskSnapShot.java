package com.ws.task.snapshot;

import com.ws.task.status.TaskStatus;
import lombok.Data;

@Data
public class TaskSnapShot<T> {
    private TaskStatus status;
    private T content;
}
