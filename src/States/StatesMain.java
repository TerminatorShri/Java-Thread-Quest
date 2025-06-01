package States;

// Extending Thread class to create a custom thread
public class StatesMain extends Thread {

    // This method will be executed when the thread is started using start()
    @Override
    public void run() {
        System.out.println("→ Thread is RUNNING (Inside run method)");

        try {
            // Puts the current thread to sleep for 2 seconds
            // During this time, the thread is in TIMED_WAITING state
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Creating a thread object – but not started yet
        StatesMain thread = new StatesMain();

        // At this point, thread is in NEW state (not started)
        System.out.println("📌 Thread State after creation: " + thread.getState()); // NEW

        // Starting the thread – it moves to RUNNABLE state
        thread.start();

        // Immediately after start(), thread is either in RUNNABLE or may quickly go to RUNNING
        System.out.println("📌 Thread State after start(): " + thread.getState()); // RUNNABLE

        // Main thread sleeps for 1 second
        // Meanwhile, our thread is sleeping for 2 seconds → it's in TIMED_WAITING
        Thread.sleep(1000);

        System.out.println("📌 Thread State after 1 second (while it's sleeping): " + thread.getState()); // TIMED_WAITING

        // Wait for the thread to finish its execution
        thread.join();

        // After completion, thread is in TERMINATED state
        System.out.println("📌 Thread State after join(): " + thread.getState()); // TERMINATED
    }
}
