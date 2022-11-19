package uppgift1;

/**
 * @author Jessica Borg
 * This class uses multithreading to demonstrate the usage of threads and their function.
 * Threads are independent - neither of the threads used in the program affects each other.
 *
 */

public class MainThread {

    public static void main (String[] args) throws InterruptedException {

        T1 thread1 = new T1(); // new Thread (extends Thread)
        T2 t2 = new T2(); // creates an instance of T2
        Thread thread2 = new Thread(t2); // creates a thread of the T2-object (implements Runnable)

        thread1.start(); // starts thread1
        thread1.join(5000); // thread1 executes for 5 seconds before thread2 can start
        thread2.start(); // thread2 starts
        thread2.join(5000); // both threads are running for 5 seconds
        thread1.stop(); // thead1 stops
        thread2.join(5000); // thread2 is executing by itself for 5 seconds
        thread2.stop(); // thread2 stops

    }
}
