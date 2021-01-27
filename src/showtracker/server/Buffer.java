package showtracker.server;

import java.util.LinkedList;

/**
 * Class for handling a buffer of Objects
 * @param <T>
 */
class Buffer<T> {
    private LinkedList<T> buffer = new LinkedList<T>();

    /**
     * Put an Object in the buffer
     * @param obj Object to put
     */
    synchronized void put(T obj) {
        buffer.addLast(obj);
        notifyAll();
    }

    /**
     * Remove an object from the buffer
     * @return The object removed
     */
    synchronized T get() {
        while(buffer.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Buffer: " + e);
            }
        }
        return buffer.removeFirst();
    }
}