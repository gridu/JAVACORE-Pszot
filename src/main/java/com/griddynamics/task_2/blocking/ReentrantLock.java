package com.griddynamics.task_2.blocking;

public class ReentrantLock {
    
    private final Object monitor = new Object();
    
    private Thread lockedBy;
    
    public void lock() throws InterruptedException {
        
        synchronized (monitor) {
            Thread callingThread = Thread.currentThread();
            while (lockedBy != null && lockedBy != callingThread) {
                monitor.wait();
            }
            lockedBy = callingThread;
        }
    }
    
    public void unlock() {
        
        synchronized (monitor) {
            Thread callingThread = Thread.currentThread();
            if (lockedBy == callingThread) {
                lockedBy = null;
                monitor.notify();
            }
        }
        
    }
    
    public void await() throws InterruptedException {
        
        synchronized (monitor) {
            monitor.wait();
        }
    }
    
    public void signal() {
        
        synchronized (monitor) {
            monitor.notify();
        }
    }
    
}
