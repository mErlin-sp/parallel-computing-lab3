import java.util.concurrent.ArrayBlockingQueue;

class Consumer implements Runnable {

    private final ArrayBlockingQueue<Integer> queue;

    public Consumer(ArrayBlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            // Витягуємо 10 елементів з черги та споживаємо їх
            for (int i = 0; i < 10; i++) {
                Integer item = queue.take();
                System.out.println("Consumer consumed: " + item);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}