package com.griddynamics.task_2;


import com.griddynamics.task_2.blocking.BlockingQueue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ThreadPool {
    
    private final BlockingQueue<Runnable> queue = new BlockingQueue<>(10);
    
    private List<WorkerThread> threads = new ArrayList<>();
    
    private boolean isDown = false;
    
    public ThreadPool(final int numOfThreads) {
        
        for (int i = 0 ; i < numOfThreads ; i++) {
            WorkerThread thread = new WorkerThread();
            thread.start();
            threads.add(thread);
        }
    }
    
    public void execute(
            @NotNull
            final Runnable task
    ) throws InterruptedException {
        
        if (isDown) {
            throw new IllegalStateException("Thread pool is turned off.");
        }
        queue.enqueue(task);
    }
    
    public synchronized void shutdown() throws InterruptedException {
        
        isDown = true;
        awaitCompletion();
    }
    
    private synchronized void awaitCompletion() throws InterruptedException {
        
        queue.close();
        for (WorkerThread thread : threads) {
            thread.join();
        }
    }
    
    private class WorkerThread extends Thread {
        
        private Runnable task;
        
        @Override
        public void run() {
            
            if (isDown && queue.isEmpty()) {
                return;
            }
            
            try {
                task = queue.dequeue();
                if (task == null) {
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            try {
                task.run();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
            
            //keep alive
            run();
            
        }
        
    }
    
}
