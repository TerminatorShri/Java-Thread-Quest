package Locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {

    // Creating a ReentrantLock (non-fair by default)
    // Internally uses AbstractQueuedSynchronizer (AQS) for queuing and lock state
    private final Lock lock = new ReentrantLock();

    // Outer method tries to acquire the lock and calls an inner method that also locks
    public void outerMethod() {
        lock.lock(); // Acquires the lock
        // Internally:
        // 1. If lock is free, thread becomes owner and sets state = 1
        // 2. If lock held by same thread, increments state (reentrant)
        // 3. If lock is held by another thread, the current thread gets enqueued (AQS)

        try {
            System.out.println("🔐 Outer method is executing");

            // This method also tries to acquire the same lock
            innerMethod(); // Works fine because of reentrancy
        } finally {
            lock.unlock(); // Always release in finally
            // Internally:
            // 1. Decrements state
            // 2. If state = 0, lock is released
            // 3. Next thread in queue is notified
        }
    }

    // Inner method also locks — demonstrates reentrancy
    public void innerMethod() {
        lock.lock(); // Allowed because ReentrantLock tracks "hold count"
        try {
            System.out.println("↪️ Inner method is executing");
        } finally {
            lock.unlock(); // Must match every lock with unlock
        }
    }

    public static void main(String[] args) {
        ReentrantLockExample example = new ReentrantLockExample();
        example.outerMethod(); // Triggers both outer and inner method calls
    }
}
