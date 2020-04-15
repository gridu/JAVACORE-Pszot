package com.griddynamics.task_2;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPool implements AutoCloseable {
    
    private static final Logger LOGGER = Logger.getLogger(ThreadPool.class.getName());
    
    private final TaskQueue taskQueue;
    
    private final PooledThread[] threads;
    
    private final int size;
    
    public ThreadPool(final int size) {
        this.size = size;
        taskQueue = new TaskQueue(size, new TaskPriorityComparator());
        threads = new PooledThread[size];
        initializeThreads();
    }
    
    private void initializeThreads() {
        for (int i = 0; i < size; i++) {
            PooledThread thread = new PooledThread();
            threads[i] = thread;
            thread.start();
        }
    }
    
    public void execute(@NotNull final Runnable task) throws InterruptedException {
        synchronized (taskQueue) {
            taskQueue.enqueue(new PoolTask(task, 1));
            taskQueue.notify();
        }
    }
    
    public void execute(@NotNull final Runnable task, final int priority) throws InterruptedException {
        synchronized (taskQueue) {
            taskQueue.enqueue(new PoolTask(task, priority));
            taskQueue.notify();
        }
    }
    
    public void execute(@NotNull final PoolTask task) throws InterruptedException {
        synchronized (taskQueue) {
            taskQueue.enqueue(task);
            taskQueue.notify();
        }
    }
    
    @Override
    public void close() {
        for (PooledThread thread : threads) {
            thread.interrupt();
        }
    }
    
    private class PooledThread extends Thread {
        
        public PooledThread() {
        
        }
        
        @Override
        public void run() {
            Runnable task;
            
            while (true) {
                synchronized (taskQueue) {
                    if (!waitForJob()) break;
                    
                    try {
                        task = taskQueue.dequeue().getTask();
                    } catch (InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "Error while task polling occurred: ", e);
                        break;
                    }
                }
                
                try {
                    task.run();
                } catch (RuntimeException e) {
                    LOGGER.log(Level.SEVERE, "Thread has been interrupted: ", e);
                }
            }
        }
        
        private boolean waitForJob() {
            synchronized (taskQueue) {
                while (taskQueue.isEmpty()) {
                    if (isInterrupted()) return false;
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "Error while queue waiting occurred: ", e);
                    }
                }
                return true;
            }
        }
        
    }
    
}
