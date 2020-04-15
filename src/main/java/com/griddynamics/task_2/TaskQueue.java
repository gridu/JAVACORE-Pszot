package com.griddynamics.task_2;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.PriorityQueue;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskQueue extends PriorityQueue<PoolTask> {
    
    private int sizeLimit;
    
    TaskQueue(final int sizeLimit, @NotNull final Comparator<PoolTask> comparator) {
        super(sizeLimit, comparator);
        this.sizeLimit = sizeLimit;
    }
    
    
    public void enqueue(@NotNull final PoolTask runnable) throws InterruptedException {
        synchronized (this) {
            while (size() == sizeLimit) {
                wait();
            }
            
            offer(runnable);
            
            if (size() == 1) {
                notifyAll();
            }
        }
    }
    
    public PoolTask dequeue() throws InterruptedException {
        synchronized (this) {
            while (size() == 0) {
                wait();
            }
            
            if (size() == sizeLimit) {
                notifyAll();
            }
            
            return poll();
        }
    }
    
    static class Lock {
    
    }
    
}
