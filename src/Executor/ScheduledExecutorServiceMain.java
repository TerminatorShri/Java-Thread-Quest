package Executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceMain {
    public static void main(String[] args) {
        /*
         * ScheduledExecutorService is a specialized ExecutorService designed to
         * execute tasks after a delay or periodically.
         *
         * Under the hood:
         * - Backed by a DelayQueue (priority queue based on scheduled time).
         * - Tasks are picked and run when their delay is over.
         * - Threads in the pool pick tasks at their scheduled execution time.
         */

        // Creating a pool with 2 threads to handle scheduled tasks
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        // ---------------------- One-time Delayed Task ------------------------
        // This schedules a task to run once after a 2-second delay
        scheduledExecutorService.schedule(() -> {
            System.out.println(Thread.currentThread().getName() + ": Task executed after 2 seconds delay");
        }, 2, TimeUnit.SECONDS);

        // ---------------------- Fixed Rate Task -----------------------------
        /*
         * scheduleAtFixedRate(): schedules a task to run repeatedly
         * - Initial delay = 0 seconds (starts immediately)
         * - Period = 3 seconds
         *
         * Important:
         * - If a task takes longer than the period, the next task may start late.
         * - Tasks are run at a *fixed rate*, not after previous completion.
         */
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + ": Task executed at fixed rate of 3 seconds");
        }, 0, 3, TimeUnit.SECONDS);

        // ---------------------- Executor Shutdown Task ----------------------
        /*
         * This schedules a shutdown after 10 seconds to stop all scheduled tasks.
         * This is important to avoid infinite execution of fixed rate tasks.
         */
        scheduledExecutorService.schedule(() -> {
            System.out.println(Thread.currentThread().getName() + ": Shutting down the executor service");
            scheduledExecutorService.shutdown();
        }, 10, TimeUnit.SECONDS);

        /*
         * Other available methods:
         * - scheduleWithFixedDelay(): runs next task after the *previous* completes and then waits the given delay.
         *   Good when tasks might have unpredictable execution times.
         *
         * Example:
         * scheduledExecutorService.scheduleWithFixedDelay(task, 1, 5, TimeUnit.SECONDS);
         * Starts after 1 sec, then 5 sec after previous completion.
         *
         * Best practice:
         * - Always shutdown your executor to avoid non-daemon threads blocking the app from exiting.
         * - Use graceful shutdown with awaitTermination() if needed.
         */
    }
}
