package Executor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceMain {

    // Simulates a CPU-intensive task with delay
    private static long calculateFactorial(int number) {
        long result = 1;
        try {
            Thread.sleep(1000); // Simulate delay to mimic long-running task
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted during factorial calculation");
            Thread.currentThread().interrupt();
        }
        for (int i = 2; i <= number; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        try {
            // ✅ Fixed thread pool of size 3 → Allows parallelism with bounded threads
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            System.out.println("Submitting factorial tasks using 3 threads...");

            for (int i = 1; i < 10; i++) {
                final int number = i;
                executorService.submit(() -> {
                    long factorial = calculateFactorial(number);
                    System.out.println("Factorial of " + number + " is: " + factorial);
                });
            }

            executorService.shutdown(); // Gracefully shutdown → no new tasks
            executorService.awaitTermination(100, TimeUnit.SECONDS); // Wait for all tasks to finish

            // Optional check loop
            while (!executorService.isTerminated()) {
                System.out.println("Waiting for factorial tasks to finish...");
            }

            // ✅ Single thread executor → Tasks are executed sequentially
            ExecutorService newExecutorService = Executors.newSingleThreadExecutor();
            System.out.println("\nSubmitting a Callable task for sum calculation...");

            // Callable returns a result
            Future<Integer> future = newExecutorService.submit(() -> {
                int sum = 0;
                for (int i = 1; i <= 10; i++) sum += i;
                return sum;
            });
            System.out.println("Sum of numbers from 1 to 10 is: " + future.get());

            // ✅ Runnable task (no return), submitted to executor
            Runnable runnable = () -> System.out.println("Hello from a runnable");
            newExecutorService.submit(runnable);
            System.out.println("Runnable task (no result) submitted successfully.");

            // ✅ Runnable task with result
            Runnable anotherRunnable = () -> System.out.println("Hello from a runnable again");
            Future<String> runnableResult = newExecutorService.submit(anotherRunnable, "Runnable task with result");
            System.out.println("Runnable task with result: " + runnableResult.get());

            // ✅ Callable task → used when we want result + can throw checked exceptions
            Callable<String> callable = () -> {
                Thread.sleep(500);
                return "Hello from a callable";
            };
            Future<String> callableFuture = newExecutorService.submit(callable);
            System.out.println("Callable task result: " + callableFuture.get());
            newExecutorService.shutdown();

            // ✅ invokeAll → waits for all tasks, returns list of Future
            ExecutorService twoThreadExecutor = Executors.newFixedThreadPool(2);
            System.out.println("\nSubmitting 3 tasks with invokeAll (2 threads in pool)...");

            Callable<Integer> task1 = () -> {
                Thread.sleep(1000);
                System.out.println("Task 1 completed.");
                return 1;
            };
            Callable<Integer> task2 = () -> {
                Thread.sleep(1000);
                System.out.println("Task 2 completed.");
                return 2;
            };
            Callable<Integer> task3 = () -> {
                Thread.sleep(1000);
                System.out.println("Task 3 completed.");
                return 3;
            };

            List<Callable<Integer>> tasks = Arrays.asList(task1, task2, task3);
            List<Future<Integer>> results = twoThreadExecutor.invokeAll(tasks, 2, TimeUnit.SECONDS);

            // Print results from invokeAll → which also accepts timeout where if tasks take longer, they are cancelled and partial results are returned
            for (int i = 0; i < results.size(); i++) {
                try {
                    System.out.println("Result from task " + (i + 1) + ": " + results.get(i).get());
                } catch (CancellationException ce) {
                    System.out.println("Task " + (i + 1) + " was cancelled due to timeout.");
                }
            }

            // ✅ invokeAny → returns result of first task to complete (others may get cancelled)
            System.out.println("\nSubmitting same tasks with invokeAny...");
            Integer anyResult = twoThreadExecutor.invokeAny(tasks, 2, TimeUnit.SECONDS);
            System.out.println("First completed task result (invokeAny): " + anyResult);

            twoThreadExecutor.shutdown();

            /*
             * 🔍 Summary of Executor Concepts:
             * - ExecutorService: manages a pool of threads to execute tasks concurrently.
             * - submit(): submits Runnable or Callable.
             * - Future: used to retrieve result or check completion.
             * - invokeAll(): waits for all tasks, returns List<Future>.
             * - invokeAny(): returns result of first successful task.
             * - shutdown(): prevents new tasks from being submitted.
             * - awaitTermination(): waits for termination.
             * - isDone(): checks if all tasks are completed.
             * - isCancelled(): checks if task was cancelled.
             * - isTerminated(): checks if executor has been shut down and all tasks completed.
             * - get(): retrieves result from Future, blocking until available else we can provide timeout to avoid blocking indefinitely.
             */

        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("\n✅ Total execution time: " + (endTime - startTime) + " ms");
        }
    }
}
