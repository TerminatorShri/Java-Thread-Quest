package Synchronization;

public class SyncThread extends Thread {
    private Counter counter; // Reference to the shared counter

    public SyncThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        // Each thread increments the counter 1000 times
        for (int i = 0; i < 1000; i++) {
            counter.increment(); // Critical section
        }
    }
}
