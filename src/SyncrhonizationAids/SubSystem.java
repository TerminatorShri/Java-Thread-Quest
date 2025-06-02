package SyncrhonizationAids;

import java.util.concurrent.CyclicBarrier;

public class SubSystem implements Runnable {
    private final String name;
    private final int initializationTime;
    private final CyclicBarrier barrier;

    public SubSystem(String name, int initializationTime, CyclicBarrier barrier) {
        this.name = name;
        this.initializationTime = initializationTime;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            System.out.println("🔧 " + name + ": Starting initialization...");
            Thread.sleep(initializationTime); // Simulate subsystem startup
            System.out.println("✅ " + name + ": Initialized. Waiting for others at barrier...");

            /*
             * await() causes this thread to wait until all parties reach this point.
             * Internally:
             * - Threads are parked using LockSupport.park() which means blocked without using wait() or sleep() until timeout or spurious wakeup.
             * - When all threads arrive, they’re unparked and the barrier action is executed by one thread
             */
            barrier.await();

            System.out.println("🚀 " + name + ": Proceeding after barrier.");
        } catch (Exception e) {
            System.err.println("❌ " + name + ": Error during synchronization: " + e.getMessage());
        }
    }
}
