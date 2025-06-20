package Package.com;
class Buffer {
    int data;
    boolean isEmpty = true;

    // Produce method
    public synchronized void produce(int value) {
        // Wait if buffer is full
        while (!isEmpty) {
            try {
                wait(); // wait until consumer consumes
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        data = value;
        isEmpty = false;
        System.out.println("Produced: " + data);
        notify(); // notify consumer
    }

    // Consume method
    public synchronized void consume() {
        // Wait if buffer is empty
        while (isEmpty) {
            try {
                wait(); // wait until producer produces
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        System.out.println("Consumed: " + data);
        isEmpty = true;
        notify(); // notify producer
    }
}

// Producer class
class Producer extends Thread {
    Buffer buffer;

    Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            buffer.produce(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

// Consumer class
class Consumer extends Thread {
    Buffer buffer;

    Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            buffer.consume();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

// Main class

public class ThreadOp {
    public static void main(String[] args) {

        Buffer sharedBuffer = new Buffer();
        Producer producer = new Producer(sharedBuffer);
        Consumer consumer = new Consumer(sharedBuffer);

        producer.start();
        consumer.start();
    }
}
