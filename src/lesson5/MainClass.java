package lesson5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static Vector<String> finishResults = new Vector<>();
    public static AtomicBoolean hasWinner = new AtomicBoolean(false);

    public static void main(String[] args) {
        System.out.println("--- >>> ПОДГОТОВКА <<< ---");
        Race race = new Race(new Road(60),
                new Tunnel(80, (int) Math.ceil(1.0 * CARS_COUNT / 2)),
                new Road(40));
        Car[] cars = new Car[CARS_COUNT];

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + Math.random() * 10);
        }
        for (Car car : cars) new Thread(car).start();

        try {
            Car.cyclicBarrier.await();
            System.out.println("--- >>> СТАРТ <<< ---");
            Car.countDownFinish.await();
            System.out.println("--- >>> ГОНКА ЗАВЕРШЕНА <<< ---\n");
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("▀▄▀▄ РЕЗУЛЬТАТЫ ▀▄▀▄");
        for (int i = 0; i < finishResults.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, finishResults.get(i)));
        }
    }
}

class Car implements Runnable {
    private static int CARS_COUNT;
    public static CyclicBarrier cyclicBarrier;
    public static CountDownLatch countDownFinish;

    static {
        CARS_COUNT = 0;
        cyclicBarrier = new CyclicBarrier(MainClass.CARS_COUNT + 1);
        countDownFinish = new CountDownLatch(MainClass.CARS_COUNT);
    }

    private Race race;
    private float speed;
    private String name;

    public double getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public Car(Race race, double speed) {
        this.race = race;
        this.speed = (float) Math.round(speed * 100) / 100;
        CARS_COUNT++;
        this.name = "Гонщик #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(name + " готов");
            cyclicBarrier.await();
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }
            System.out.println("▀▄▀▄ " + name + " финишировал!");
            MainClass.finishResults.add(String.format("%s (скорость: %.2f)", name, speed));
            if (!MainClass.hasWinner.get()) {
                MainClass.hasWinner.set(true);
                System.out.println("--- >>> " + name.toUpperCase() + " ПОБЕДИЛ! <<< ---");
            }
            countDownFinish.countDown();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

abstract class Stage {
    private int length;
    private String description;

    public Stage(int length, String name) {
        this.length = length;
        this.description = name + " " + length + " метров";
    }

    public String getDescription() {
        return description;
    }

    public void go(Car car) throws InterruptedException {
        System.out.println(car.getName() + " начал этап: " + description);
        long time = (long) (length / car.getSpeed() * 1000);
        Thread.sleep(time);
        System.out.println(car.getName() + " закончил этап: " + description);
    }
}

class Road extends Stage {

    public Road(int length) {
        super(length, "Дорога");
    }

    @Override
    public void go(Car car) throws InterruptedException {
        super.go(car);
    }
}

class Tunnel extends Stage {
    private Semaphore semaphoreCapacity;

    public Tunnel(int length, int capacity) {
        super(length, "Туннель");
        semaphoreCapacity = new Semaphore(capacity);
    }

    @Override
    public void go(Car car) throws InterruptedException {
        System.out.println(car.getName() + " готовится к этапу(ждет): " + getDescription());
        semaphoreCapacity.acquire();
        super.go(car);
        semaphoreCapacity.release();
    }
}

class Race {
    private ArrayList<Stage> stages;

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
    }
}