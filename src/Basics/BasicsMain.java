package Basics;

public class BasicsMain {
    public static void main(String[] args) {
        // Create an instance of a thread by extending the Thread class
        ExtendingThreadClass extendingThreadClassThread = new ExtendingThreadClass();

        // Start the thread – this calls the start() method from the Thread class,
        // which creates a new OS-level thread and then calls the run() method internally
        extendingThreadClassThread.start();

        // Create an instance of a class that implements Runnable
        ImplementingRunnableInterface implementingRunnableInterfaceThread = new ImplementingRunnableInterface();

        // Wrap the Runnable object in a Thread object.
        // This separates task definition (Runnable) from task execution (Thread)
        Thread runnableThread = new Thread(implementingRunnableInterfaceThread);

        // Start the thread which will call run() on the Runnable object
        runnableThread.start();

        // This loop runs on the main thread
        for(int i = 1; i <= 5; i++) {
            System.out.println("Main thread: i = " + i);
        }
    }
}
