package Communication;

import java.util.logging.Logger;

// Shared resource class used by Producer and Consumer threads
public class SharedResources {
    private int value;              // Shared data item between producer and consumer
    private boolean isAvailable;    // Flag to indicate if value is ready to consume

    // Method used by producer to set a value
    public synchronized void produce(int value) {
        // If value is already produced and not yet consumed, wait until it's consumed
        while (isAvailable) {
            try {
                // Causes current thread to wait and release the monitor (lock) of this object
                // Monitor is an intrinsic lock that must be acquired by the thread and only one thread can hold it at a time while entering synchronized block
                wait();
                // When notified, the thread reacquires the lock and checks the condition again
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Interrupted while waiting for value to become available");
                Thread.currentThread().interrupt();
            }
        }

        // Set the new value
        this.value = value;
        System.out.println("Produced " + value);

        // Mark it available to consume
        isAvailable = true;

        // Wake up a waiting thread (usually consumer) that is waiting on this object's monitor
        notify(); // ✅ notify wakes up *one* waiting thread

        /**
         * 🔍 Why `notify()` and not `notifyAll()` here?
         * - There is only ONE producer and ONE consumer.
         * - So, only ONE thread (the consumer) is waiting — no need to wake up others.
         *
         * 🚨 If there were MULTIPLE consumers (or producers), use `notifyAll()` instead:
         *     notifyAll(); // Wakes up ALL threads waiting on this monitor object.
         *     - Safer in scenarios with multiple waiting threads.
         *     - Prevents thread starvation or missed signals.
         */
    }

    // Method used by consumer to consume the value
    public synchronized int consume() {
        // If value is not yet produced, wait until producer produces it
        while (!isAvailable) {
            try {
                wait(); // Wait for producer to notify after producing a value
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Interrupted while waiting for value to be produced");
                Thread.currentThread().interrupt();
            }
        }

        // Consume the value
        isAvailable = false; // Mark as consumed
        System.out.println("Consumed " + value);

        notify(); // Wake up producer so it can produce next item

        // Same `notify()` vs `notifyAll()` logic applies here as well

        return value;
    }
}
