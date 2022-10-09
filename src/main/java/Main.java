import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public final static int PEOPLE = 3;
    public final static int PEOPLE_ATC = 60;
    public final static int SIZE = 20;

    public final static int TIME_WORK = 3000;
    public final static int TIME_ATC_WAIT = 1000;

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> call = new ArrayBlockingQueue<>(SIZE);


        Thread atc = new Thread(() -> {
            System.out.printf("Я, %s, позвонил в службу поддержки\n", Thread.currentThread().getName());
            try {
                call.put(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                return;
            }
        });

        Thread operator = new Thread(() -> {
            System.out.printf("Привет! Я %s готов принимать звонки!\n", Thread.currentThread().getName());
            try {
                do {
                    System.out.printf("%s обработал заказ. %s свободен.\n", Thread.currentThread().getName(), call.take());
                    Thread.sleep(TIME_WORK);
                    if (call.isEmpty())
                        Thread.sleep(TIME_WORK);
                } while (!call.isEmpty());
                System.out.printf("Звонков больше нет! %s ушел домой\n", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                return;
            }
        });

        for (int i = 1; i <= PEOPLE; i++) {
            Thread thread = new Thread(new Thread(operator));
            thread.setName("Оператор-" + i);
            thread.start();
        }

        for (int i = 1; i <= PEOPLE_ATC; i++) {
            Thread thread = new Thread(new Thread(atc));
            thread.setName("Клиент-" + i);
            Thread.sleep(TIME_ATC_WAIT);
            thread.start();
        }
    }
}
