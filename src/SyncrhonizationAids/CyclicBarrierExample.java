package SyncrhonizationAids;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {
    public static void main(String[] args) {
        int numOfSubSystems = 4;

        /*
         * CyclicBarrier is initialized with:
         * - The number of parties (threads) that must call await() before all are released.
         * - A barrier action (Runnable) that runs once all parties arrive at the barrier.
         *
         * Under the hood:
         * - It uses a Generation object to track barrier cycles.
         * - Once all parties call await(), the generation advances and all waiting threads are released.
         * - The barrier is cyclic, meaning it can be reused after all threads cross it.
         */
        CyclicBarrier barrier = new CyclicBarrier(numOfSubSystems, () -> {
            System.out.println("🔔 All subsystems are initialized. System is starting up!");
        });

        // Create and start threads representing each subsystem
        Thread webServerThread = new Thread(new SubSystem("Web Server", 2000, barrier));
        Thread databaseThread = new Thread(new SubSystem("Database", 3000, barrier));
        Thread cacheThread = new Thread(new SubSystem("Cache", 1000, barrier));
        Thread messageQueueThread = new Thread(new SubSystem("Message Queue", 1500, barrier));

        System.out.println("Main: Starting initialization of subsystems...");
        webServerThread.start();
        databaseThread.start();
        cacheThread.start();
        messageQueueThread.start();
    }
}
