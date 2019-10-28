package lesson4.MFP;

import java.util.Scanner;

public class Organization {
    public static void main(String[] args) {
        try {
            new Organization();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Organization() throws InterruptedException {
        MFP mfp = new MFP();
        Thread.sleep(1000);
        System.out.println("Начинаем работу");
        Scanner scanner = new Scanner(System.in);
        int i = 0;
        while (true) {
            scanner.nextLine();
            mfp.sendToPrinter(new EDocument("Doc" + i, 5),2);
            i++;
            scanner.nextLine();
            mfp.getFromScanner(new PaperDocument("Doc" + i, 5));
            i++;
            scanner.nextLine();
            mfp.makeCopies(new PaperDocument("Doc" + i, 5),5);
            i++;
        }
    }
}
