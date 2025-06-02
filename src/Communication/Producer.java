package Communication;

public class Producer implements Runnable {
    private final SharedResources sharedResources;

    // Takes shared resource as input
    public Producer(SharedResources sharedResources) {
        this.sharedResources = sharedResources;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            sharedResources.produce(i);  // Produce item
            try {
                Thread.sleep(100);  // Simulate production time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
