package Locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class BankAccount {
    private int balance = 100;

    // ReentrantLock allows manual locking/unlocking with more control (like timeout, interrupt handling, fairness)
    private final Lock lock = new ReentrantLock();

    // 🔒 synchronized method: implicit monitor lock on `this`
    public synchronized void synchronizedWithdraw(int amount) {
        System.out.println(Thread.currentThread().getName() + " attempting to withdraw " + amount + " from balance: " + balance);
        performTransaction(amount); // Unsafe: shared for both methods; assume it's synchronized or guarded!
    }

    // 🔐 Locking using ReentrantLock (explicit lock)
    public void lockingWithdraw(int amount) {
        System.out.println(Thread.currentThread().getName() + " attempting to withdraw " + amount + " from balance: " + balance);

        try {
            // tryLock with timeout — prevents thread starvation and deadlock risk
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                try {
                    performTransaction(amount);
                } finally {
                    // Always release the lock to avoid deadlocks
                    lock.unlock();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire lock within timeout for withdrawal of " + amount);
            }
        } catch (InterruptedException e) {
            Logger.getGlobal().warning("Interrupted while waiting for lock");
            Thread.currentThread().interrupt(); // Restore interrupt status
        }

        if (Thread.currentThread().isInterrupted()) {
            System.out.println(Thread.currentThread().getName() + " was interrupted during withdrawal attempt.");
        }
    }

    // This method assumes the caller holds the lock!
    private void performTransaction(int amount) {
        if (balance >= amount) {
            System.out.println(Thread.currentThread().getName() + " withdrawing " + amount);
            try {
                Thread.sleep(1000); // Simulating transaction time
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Interrupted during withdrawal processing");
                Thread.currentThread().interrupt();
            }
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " completed withdrawal. New balance: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() + " ❌ Insufficient balance for withdrawal of " + amount);
        }
    }
}
