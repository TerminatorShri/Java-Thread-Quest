package Communication;

public class ThreadCommunication {
    public static void main(String[] args) {
        SharedResources sharedResources = new SharedResources();

        // Create producer and consumer threads using shared resource
        Thread producerThread = new Thread(new Producer(sharedResources));
        Thread consumerThread = new Thread(new Consumer(sharedResources));

        // Start both threads
        producerThread.start();
        consumerThread.start();

        try {
            // Wait for both threads to finish
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
