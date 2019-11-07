package lesson7.seaports;

import java.util.concurrent.Semaphore;

public class SeaChannel {
    private final int TIME_TO_PASS = 3000;
    private final int SHIPS_LIMIT = 2;
    private final int TIME_NEAR_CHANNEL = 1000;

    private Semaphore semaphore;

    public SeaChannel() {
        semaphore = new Semaphore(SHIPS_LIMIT);
    }

    public void passThrough(Ship ship) throws InterruptedException {
        String shipName = ship.getShipName();

        System.out.println(String.format("Корабль '%s':\tплывет к каналу", shipName));
        Thread.sleep(TIME_NEAR_CHANNEL);
        System.out.println(String.format("Корабль '%s':\tожидает разрешения на заход в канал", shipName));
        semaphore.acquire();
        System.out.println(String.format("Корабль '%s':\tзашел в канал", shipName));
        Thread.sleep(TIME_TO_PASS);
        System.out.println(String.format("Корабль '%s':\tвышел из канала", shipName));
        semaphore.release();
        Thread.sleep(TIME_NEAR_CHANNEL);
    }
}
