package SyncrhonizationAids;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

class DependentServiceLatch implements Callable<String> {
    private final CountDownLatch latch;

    public DependentServiceLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public String call() throws Exception {
        try {
            System.out.println(Thread.currentThread().getName() + ": Starting dependent service...");
            Thread.sleep((long) (Math.random() * 1000)); // Simulate some processing
            System.out.println(Thread.currentThread().getName() + ": Dependent service completed execution.");
        } finally {
            latch.countDown(); // Decrease the latch count
            System.out.println(Thread.currentThread().getName() + ": Latch count decreased. Remaining: " + latch.getCount());
        }
        return "Dependent Service Completed";
    }
}
