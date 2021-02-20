package com.griddynamics.task_2.blocking;


import java.util.LinkedList;
import java.util.Queue;


public class BlockingQueue<T> {
  
  private boolean closed;
  
  private Queue<T> queue = new LinkedList<>();
  
  private int capacity;
  
  private ReentrantLock putLock = new ReentrantLock();
  
  private ReentrantLock takeLock = new ReentrantLock();
  
  public BlockingQueue(final int capacity) {
    
    this.capacity = capacity;
  }
  
  public void enqueue(final T element) throws InterruptedException {
    
    putLock.lock();
    try {
      while (isFull()) {
        putLock.await();
      }
      queue.offer(element);
      
      if (queue.size() == 1) {
        takeLock.signal();
      }
    } finally {
      putLock.unlock();
    }
  }
  
  public T dequeue() throws InterruptedException {
    
    takeLock.lock();
    try {
      while (isEmpty()) {
        if (closed) {
          return null;
        }
        takeLock.await();
      }
      
      T poll = queue.poll();
      putLock.signal();
      
      return poll;
    } finally {
      takeLock.unlock();
    }
  }
  
  public boolean isFull() {
    
    return queue.size() == capacity;
  }
  
  public boolean isEmpty() {
    
    return queue.isEmpty();
  }
  
  public void close() {
    
    closed = true;
    putLock.signal();
    takeLock.signal();
  }
  
}
