package SyncrhonizationAids;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicOnly {
    // AtomicInteger ensures both visibility and atomicity for concurrent updates
    // Uses CPU-level CAS (Compare-And-Swap) and volatile memory semantics
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting AtomicInteger Reader/Writer Example...");

        // Writer thread: increments the counter 1000 times
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                int newValue = atomicCounter.incrementAndGet(); // Atomic operation
                if (i % 200 == 0) {
                    System.out.println("[Writer] Counter incremented to: " + newValue);
                }
            }
        });

        // Reader thread: reads the counter periodically
        Thread reader = new Thread(() -> {
            int lastSeen = -1;
            for (int i = 0; i < 10; i++) {
                int currentValue = atomicCounter.get(); // Safe read
                if (currentValue != lastSeen) {
                    System.out.println("[Reader] Current counter value: " + currentValue);
                    lastSeen = currentValue;
                }
                try {
                    Thread.sleep(100); // Simulate periodic reading
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();

        System.out.println("Final counter value: " + atomicCounter.get());
    }
}
