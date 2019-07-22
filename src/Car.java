import java.util.concurrent.CountDownLatch;

public class Car implements Runnable {
    static boolean winner = true;
    CountDownLatch cdlStart;
    CountDownLatch cdlFinish;
    CountDownLatch cdlPrintMassage;
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CountDownLatch cdlStart, CountDownLatch cdlFinish, CountDownLatch cdlPrintMassage) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cdlStart = cdlStart;
        this.cdlFinish = cdlFinish;
        this.cdlPrintMassage = cdlPrintMassage;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cdlStart.countDown();
            cdlStart.await();
            cdlPrintMassage.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        cdlFinish.countDown();
        if(winner){
            winner = false;
            System.out.println(this.name + " - WINNER");
            try {
                cdlFinish.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.name + " - RACE WINNER");
        }
    }
}

