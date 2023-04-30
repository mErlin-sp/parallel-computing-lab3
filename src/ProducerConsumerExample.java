import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ForkJoinPool;

public class ProducerConsumerExample {

    public static void main(String[] args) throws InterruptedException {
        // Створюємо блокуючу чергу з обмеженням на розмір 10
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        // Створюємо відділення ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();

        // Створюємо продюсера та консюмера
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        // Використовуємо ForkJoinPool для запуску задач продюсера та консюмера
        pool.execute(producer);
        pool.execute(consumer);

        // Очікуємо завершення всіх задач
        pool.shutdown();
        Thread.sleep(5000);
    }
}




