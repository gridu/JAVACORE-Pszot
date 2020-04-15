package com.griddynamics.task_2;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class PoolTask {
    
    private final Runnable task;
    
    private final int priority;
    
    public PoolTask(@NotNull final Runnable task, final int priority) {
        this.task = task;
        this.priority = priority;
    }
    
}
