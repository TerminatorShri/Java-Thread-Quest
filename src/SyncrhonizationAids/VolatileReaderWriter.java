package SyncrhonizationAids;

public class VolatileReaderWriter {
    // Shared data marked as volatile so updates by one thread are immediately visible to others
    private volatile boolean flag = false;

    public void writer() {
        System.out.println("Writer thread: setting flag to true...");
        flag = true; // Write to volatile variable
        System.out.println("Writer thread: flag set to true.");
    }

    public void reader() {
        System.out.println("Reader thread: waiting for flag to become true...");
        while (!flag) {
            // Busy-wait until flag is true
            // Without 'volatile', this might loop forever due to thread-local caching
        }
        System.out.println("Reader thread: detected flag is true. Proceeding...");
    }

    public static void main(String[] args) {
        VolatileReaderWriter shared = new VolatileReaderWriter();

        // Reader thread starts first and waits until flag becomes true
        Thread readerThread = new Thread(shared::reader);
        readerThread.start();

        // Writer thread sets the flag after a delay
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            shared.writer();
        });
        writerThread.start();
    }
}
