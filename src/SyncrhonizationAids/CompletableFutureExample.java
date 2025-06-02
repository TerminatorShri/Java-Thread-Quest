package SyncrhonizationAids;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class CompletableFutureExample {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("Main thread started: " + Thread.currentThread().getName());

        // Run three asynchronous tasks using supplyAsync
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
            simulateWork("Task 1");
            return ThreadLocalRandom.current().nextInt(100);
        });

        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            simulateWork("Task 2");
            return ThreadLocalRandom.current().nextInt(100);
        });

        CompletableFuture<Integer> task3 = CompletableFuture.supplyAsync(() -> {
            simulateWork("Task 3");
            return ThreadLocalRandom.current().nextInt(100);
        });

        // Wait for all tasks to complete using allOf
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2, task3);

        // When all tasks are done, combine their results
        CompletableFuture<Integer> finalResult = allTasks.thenApply(v -> {
            try {
                int sum = task1.get() + task2.get() + task3.get();
                System.out.println("All tasks completed. Sum = " + sum);
                return sum;
            } catch (Exception e) {
                throw new RuntimeException("Failed to get task results", e);
            }
        });

        // Block and get the final combined result
        System.out.println("Final combined result = " + finalResult.get());
        System.out.println("Main thread ended.");
    }

    // Helper method to simulate work with a sleep
    private static void simulateWork(String taskName) {
        try {
            System.out.println(taskName + " running in thread: " + Thread.currentThread().getName());
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
            System.out.println(taskName + " completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(taskName + " was interrupted.");
        }
    }
}
