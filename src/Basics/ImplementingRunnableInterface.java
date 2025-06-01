package Basics;

// This class demonstrates thread creation by implementing the Runnable interface
public class ImplementingRunnableInterface implements Runnable {
    @Override
    public void run() {
        // This is the task that will be run by a separate thread
        for (int i = 1; i <= 5; i++) {
            System.out.println("Implementing Runnable Interface i = " + i);
        }
    }
}
