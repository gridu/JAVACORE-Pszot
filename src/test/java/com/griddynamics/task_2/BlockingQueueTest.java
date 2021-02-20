package com.griddynamics.task_2;


import com.griddynamics.task_2.blocking.BlockingQueue;
import lombok.SneakyThrows;
import org.testng.annotations.Test;
import org.testng.internal.thread.ThreadTimeoutException;

import static org.testng.Assert.assertTrue;


public class BlockingQueueTest {
    
    private final int QUEUE_CAPACITY = 5;
    
    BlockingQueue<Runnable> queue = new BlockingQueue<>(QUEUE_CAPACITY);
    
    @Test
    @SneakyThrows
    public void shouldEnqueue() {
        
        fillQueue();
        assertTrue(queue.isFull());
    }
    
    @Test( dependsOnMethods = "shouldEnqueue" )
    @SneakyThrows
    public void shouldDequeue() {
        
        for (int i = 0 ; i < QUEUE_CAPACITY ; i++) {
            queue.dequeue();
        }
        
        assertTrue(queue.isEmpty());
    }
    
    @Test( timeOut = 100, expectedExceptions = ThreadTimeoutException.class, dependsOnMethods = "shouldDequeue" )
    @SneakyThrows
    public void shouldWaitIfEmpty() {
        
        queue.dequeue();
    }
    
    @Test( timeOut = 100, expectedExceptions = ThreadTimeoutException.class, dependsOnMethods = "shouldWaitIfEmpty" )
    @SneakyThrows
    public void shouldWaitIfFull() {
        
        fillQueue();
        queue.enqueue(() -> System.out.println("full"));
    }
    
    private void fillQueue() throws InterruptedException {
        
        for (int i = 0 ; i < QUEUE_CAPACITY ; i++) {
            queue.enqueue(() -> System.out.println("enqueued"));
        }
    }
    
}
