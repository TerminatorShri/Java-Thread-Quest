package Locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {
    private int counter = 0; // Shared resource

    // ReadWriteLock allows:
    // - Multiple threads to read simultaneously
    // - Only one thread to write at a time
    // - No readers allowed while writing (write is exclusive)
    // - Used to create same lock state for both read and write operations
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    // Separate locks for read and write
    private final Lock readLock = lock.readLock();   // Can be acquired by many threads concurrently
    private final Lock writeLock = lock.writeLock(); // Exclusive: blocks all reads & writes

    // Method to increment counter
    public void increment() {
        writeLock.lock(); // Acquire exclusive write lock
        try {
            // Only one thread can enter this block at a time
            counter++;
        } finally {
            writeLock.unlock(); // Must release the lock to avoid deadlock
        }
    }

    // Method to read the counter value
    public int getCounter() {
        readLock.lock(); // Allows multiple concurrent reads
        try {
            return counter;
        } finally {
            readLock.unlock(); // Always unlock in finally
        }
    }

    public static void main(String[] args) {
        ReadWriteLockExample example = new ReadWriteLockExample();

        // Multiple reader threads — can read concurrently
        Runnable readTask = () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("Read: " + example.getCounter());
            }
        };

        // Single writer thread — only one at a time, blocks readers during writes
        Runnable writeTask = () -> {
            for (int i = 0; i < 100; i++) {
                example.increment();
                System.out.println("Incremented: " + example.getCounter());
            }
        };

        Thread writerThread = new Thread(writeTask);
        Thread readerThread1 = new Thread(readTask);
        Thread readerThread2 = new Thread(readTask);

        // Start all threads
        writerThread.start();
        readerThread1.start();
        readerThread2.start();

        // Wait for all threads to complete
        try {
            writerThread.join();
            readerThread1.join();
            readerThread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupt status
        } finally {
            System.out.println("Final Counter Value: " + example.getCounter());
        }
    }
}
