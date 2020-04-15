package com.griddynamics.task_2;

import org.testng.annotations.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPoolTest {
    
    private static final Logger LOGGER = Logger.getLogger(ThreadPoolTest.class.getName());
    
    @Test
    public void shouldExecuteInParallel() throws InterruptedException {
        ThreadPool pool = new ThreadPool(5);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.1 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.2 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.3 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.4 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.5 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.6 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.7 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.8 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.9 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.10 is running"), 1);
        pool.execute(() -> LOGGER.log(Level.INFO, "Task no.11 is running"), 1);
        pool.close();
    }
    
}
