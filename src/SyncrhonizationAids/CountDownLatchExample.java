package SyncrhonizationAids;

import java.util.concurrent.*;

public class CountDownLatchExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int numOfDependentServices = 3;

        // Latch initialized with count = 3
        CountDownLatch countDownLatch = new CountDownLatch(numOfDependentServices);

        ExecutorService executorService = Executors.newFixedThreadPool(numOfDependentServices);

        System.out.println("Main thread: Submitting dependent services...");

        Future<String> future1 = executorService.submit(new DependentService(countDownLatch));
        Future<String> future2 = executorService.submit(new DependentService(countDownLatch));
        Future<String> future3 = executorService.submit(new DependentService(countDownLatch));

        // Main thread waits here until latch count becomes 0
        System.out.println("Main thread: Waiting for all dependent services to complete...");
        countDownLatch.await(); // Blocks until all services finish and countDown() is called 3 times

        // All dependent services are done, now we can fetch their results
        System.out.println("Main thread: All dependent services completed. Starting main service...");

        System.out.println("Result 1: " + future1.get());
        System.out.println("Result 2: " + future2.get());
        System.out.println("Result 3: " + future3.get());

        executorService.shutdown();
    }
}
