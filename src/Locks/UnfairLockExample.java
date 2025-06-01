package Locks;

import java.util.concurrent.locks.ReentrantLock;

/*
* Threads are added to an internal queue (AbstractQueuedSynchronizer - AQS).
* But: New threads can acquire the lock even if others are waiting (called barging).
* This can lead to thread starvation — some threads wait forever while others sneak in.
* */

public class UnfairLockExample {

    // Create an unfair lock (default constructor -> unfair by default)
    // Threads can barge in even if others are waiting
    private final ReentrantLock lock = new ReentrantLock(); // unfair by default

    public void accessResource() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " attempting to acquire lock");

        lock.lock(); // Does not guarantee FIFO order
        try {
            System.out.println(threadName + " acquired lock and is working");

            // Simulate long work to cause contention
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(threadName + " was interrupted");
            }

        } finally {
            System.out.println(threadName + " releasing lock");
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        UnfairLockExample example = new UnfairLockExample();

        // Start 5 threads trying to access the same lock
        for (int i = 1; i <= 5; i++) {
            Thread thread = new Thread(example::accessResource, "Thread-" + i);
            thread.start();
        }
    }
}
