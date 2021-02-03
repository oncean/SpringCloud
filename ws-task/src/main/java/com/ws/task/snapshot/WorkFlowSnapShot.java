package com.ws.task.snapshot;

import com.ws.task.AbstractTask;
import com.ws.task.WorkFlow;
import com.ws.task.status.WorkflowStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkFlowSnapShot {
    private WorkflowStatus status;
    private String workflowId;
    private int currentStep;
    private List<TaskSnapShot> steps = new ArrayList<>();

    public WorkFlowSnapShot(WorkFlow workFlow){
        this.status = workFlow.getStatus();
        this.workflowId = workFlow.getWorkFlowId();
        this.currentStep = workFlow.getCurrentStep();
        List<AbstractTask> tasks = workFlow.getSteps();
        for (AbstractTask task :
                tasks) {
            steps.add(task.snapshot());
        }
    }
}
