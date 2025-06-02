package Communication;

public class Consumer implements Runnable {
    private final SharedResources sharedResources;

    // Takes shared resource as input
    public Consumer(SharedResources sharedResources) {
        this.sharedResources = sharedResources;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            int value = sharedResources.consume();  // Consume item
            try {
                Thread.sleep(150);  // Simulate consumption time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
