package Basics;

// This class demonstrates thread creation by extending the Thread class
public class ExtendingThreadClass extends Thread {
    // The run() method defines the code that the new thread will execute
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("Extending Thread Class i = " + i);
        }
    }
}
