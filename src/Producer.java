import java.util.concurrent.ArrayBlockingQueue;

class Producer implements Runnable {

    private final ArrayBlockingQueue<Integer> queue;

    public Producer(ArrayBlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            // Виробляємо 10 елементів та додаємо їх у чергу
            for (int i = 0; i < 10; i++) {
                queue.put(i);
                System.out.println("Producer produced: " + i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}