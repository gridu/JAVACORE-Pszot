package com.griddynamics.task_2;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<PoolTask> {
    
    @Override
    public int compare(@NotNull final PoolTask left, @NotNull final PoolTask right) {
        return Integer.compare(left.getPriority(), right.getPriority());
    }
    
}
