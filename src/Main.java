import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Main {
    private static final int CARS_COUNT = 4;
    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(
                new Road(60),
                new Tunnel(new Semaphore(CARS_COUNT/2)),
                new Road(40)
        );
        Car[] cars = new Car[CARS_COUNT];
        final CountDownLatch cdlStart = new CountDownLatch(CARS_COUNT);
        // cdlStart ожидает пока все будут готовы
        final CountDownLatch cdlFinish = new CountDownLatch(CARS_COUNT);
        // cdlFinish ожидает пока все закончат, чтобы вывести msg
        final CountDownLatch cdlPrintMassage = new CountDownLatch(1);
        // cdlPrintMassage ожидает, пока не будет выведено сообщение о начале гонки,
        // после чего только запускает гонку. (Чтобы не было, что другой поток стартанул быстрее чем Main)
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cdlStart, cdlFinish, cdlPrintMassage);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            cdlStart.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            cdlPrintMassage.countDown();
            cdlFinish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
