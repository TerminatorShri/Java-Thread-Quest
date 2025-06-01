package Synchronization;

public class Counter {
    private int count = 0;

    // ✅ Synchronized Method
    // Only one thread can access increment() on this object at a time
    // JVM checks monitor of the object (monitor = intrinsic lock). If free → acquire → run → release.
    public synchronized void increment() {
        count++; // Critical section: read-modify-write of shared state
    }

    // ✅ Synchronized Block (fine-grained control)
    public void decrement() {
        synchronized (this) {
            count--;
        }
    }

    public int getCount() {
        return count;
    }
}
