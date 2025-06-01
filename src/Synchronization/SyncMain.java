package Synchronization;

public class SyncMain {
    public static void main(String[] args) {
        // Shared resource - counter object
        Counter counter = new Counter();

        // Two threads working on the same shared counter
        SyncThread thread1 = new SyncThread(counter);
        SyncThread thread2 = new SyncThread(counter);

        // Start both threads
        thread1.start();
        thread2.start();

        // Wait for both threads to complete before printing final result
        try {
            thread1.join(); // Main thread waits for thread1 to finish
            thread2.join(); // Main thread waits for thread2 to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final value should be 2000 if synchronization worked correctly
        System.out.println("✅ Final count: " + counter.getCount());
    }
}
