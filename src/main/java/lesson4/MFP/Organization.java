package lesson4.MFP;

import java.util.Random;

public class Organization {
    public static void main(String[] args) {
        try {
            new Organization();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final int MIN_DELAY = 2000;
    private final int MAX_DELAY = 15000;
    private final int MAX_PAGES = 10;
    private final int MAX_COPIES = 3;

    Random rnd = new Random();
    private MFP mfp;
    static int index=0;

    public Organization() throws InterruptedException {
        mfp = new MFP();
        Thread.sleep(500);
        System.out.println("Начинаем работу");
        int i = 0;
        while (true) {
            getRandomTask();
            getRandomDelay();
        }
    }

    private void getRandomTask() {
        int task = rnd.nextInt(3);
        int pages = 1 + rnd.nextInt(MAX_PAGES);
        int copies = 1 + rnd.nextInt(MAX_COPIES);
        switch (task) {
            case (0):
                mfp.sendToPrinter(new EDocument("Document" + index, pages), copies);
                break;
            case (1):
                mfp.getFromScanner(new PaperDocument("Document" + index, pages));
                break;
            case (2):
                mfp.makeCopies(new PaperDocument("Document" + index, pages), copies);
                break;
        }
    }

    private void getRandomDelay() {
        try {
            Thread.sleep(MIN_DELAY + rnd.nextInt(MAX_DELAY - MIN_DELAY));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
