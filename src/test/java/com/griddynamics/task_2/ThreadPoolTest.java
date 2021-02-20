package com.griddynamics.task_2;


import lombok.SneakyThrows;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class ThreadPoolTest {
    
    private final int THREAD_POOL_SIZE = 3;
    private final int TIMEOUT = 1;
    
    
    @Test
    public void shouldRunTasksOnMultipleThreads() throws InterruptedException {
        
        int startingThreadCount = Thread.activeCount();
        
        ThreadPool threadPool = new ThreadPool(THREAD_POOL_SIZE);
        
        assertEquals(Thread.activeCount(), THREAD_POOL_SIZE + startingThreadCount);
        
        for (int i = 0 ; i < 10 ; i++) {
            threadPool.execute(new Task(TIMEOUT));
        }
        
        threadPool.shutdown();
        
        assertEquals(Thread.activeCount(), startingThreadCount);
    }
    
    
    private static class Task implements Runnable {
        
        private final long timeout;
        
        public Task(long timeout) {
            
            this.timeout = timeout;
        }
        
        @SneakyThrows
        @Override
        public void run() {
            
            Thread.sleep(timeout);
        }
        
    }
    
}
