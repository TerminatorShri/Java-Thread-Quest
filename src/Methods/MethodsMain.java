package Methods;

public class MethodsMain extends Thread {

    public MethodsMain(String name) {
        super(name); // ✅ Sets the thread name using parent class constructor (Thread)
    }

    @Override
    public void run() {
        // ✅ This code runs inside the new thread's call stack when start() is called
        for (int i = 1; i <= 5; i++) {

            // Simulate CPU-heavy task (force context switch, memory use)
            StringBuilder temp = new StringBuilder();
            temp.append("a".repeat(100000)); // Uses heap memory, forces thread to do work

            System.out.println("🔹 " + Thread.currentThread().getName() +
                    " | Priority: " + Thread.currentThread().getPriority() +
                    " | Iteration: " + i);

            // Demonstrating voluntary CPU release using yield
            if (i == 3) {
                System.out.println("⏸️ " + Thread.currentThread().getName() + " is yielding at i = 3");
                Thread.yield(); // Hint to CPU scheduler: "I'm willing to pause, let others run"
            }

            try {
                // JVM changes thread state to TIMED_WAITING and schedules wake-up
                Thread.sleep(50); // ✅ Thread goes into TIMED_WAITING state for 50ms
            } catch (InterruptedException e) {
                System.err.println("❗ " + Thread.currentThread().getName() + " was interrupted.");
                return; // Exit early if interrupted
            }
        }

        System.out.println("✅ " + Thread.currentThread().getName() + " has finished execution.");
    }

    public static void main(String[] args) throws InterruptedException {

        // ✅ Create user threads
        MethodsMain t1 = new MethodsMain("Thread-1-Low");
        MethodsMain t2 = new MethodsMain("Thread-2-Med");
        MethodsMain t3 = new MethodsMain("Thread-3-High");

        // ✅ Set thread priorities (works as a "suggestion" to OS scheduler), not guaranteed behavior
        t1.setPriority(Thread.MIN_PRIORITY); // 1
        t2.setPriority(Thread.NORM_PRIORITY); // 5 (default)
        t3.setPriority(Thread.MAX_PRIORITY); // 10

        // ✅ Create a background daemon thread
        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("🛠️ Daemon thread running in background...");
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    break; // Exit loop if interrupted
                }
            }
        }, "Background-Daemon");

        daemonThread.setDaemon(true); // ✅ Must be called BEFORE start()
        daemonThread.start(); // ✅ JVM creates a new thread that runs in background

        // ✅ Start user threads
        t1.start();
        t2.start();
        t3.start();

        // ✅ Interrupt one thread after 100ms
        Thread.sleep(100);
        t2.interrupt(); // Sends a signal to t2, which will throw InterruptedException if sleeping or waiting

        // ✅ Wait for all user threads to complete
        t1.join(); // Main thread waits for t1
        t2.join();
        t3.join();

        // ✅ Once all user threads finish, JVM will kill daemon thread and exit
        System.out.println("💡 All user threads have finished. JVM will now exit.");
    }
}
