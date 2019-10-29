package lesson4;

import java.util.ArrayList;

public class ABC {
    private static final int REPEATS_COUNT = 5;

    public static void main(String[] args) {
        char[] letters = {'A', 'B', 'C'};
        ArrayList<Letter> list = new ArrayList<>();
        for (int i = 0; i < letters.length; i++) {
            list.add(new Letter(letters[i], i == 0));
        }
        for (int i = 0; i < letters.length; i++) {
            list.get(i).setNext(list.get(i == letters.length - 1 ? 0 : i + 1));
            list.get(i).start();
        }
    }

    private static class Letter extends Thread {
        private char c;
        private Letter next;
        private boolean isEnable;

        Letter(char c, boolean isEnable) {
            this.c = c;
            this.isEnable = isEnable;
        }

        void setNext(Letter next) {
            this.next = next;
        }

        @Override
        public void run() {
            try {
                for (int count = 0; count < REPEATS_COUNT; count++) print(this, next);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        char getChar() {
            return c;
        }

        private synchronized static void print(Letter letter, Letter next) throws InterruptedException {
            while (!letter.isEnable) Letter.class.wait();
            System.out.print(letter.getChar());
            next.isEnable = true;
            letter.isEnable = false;
            Letter.class.notifyAll();
        }
    }
}

//public class ABC {
//    public static void main(String[] args) {
//        new letter('A', 1);
//        new letter('B', 2);
//        new letter('C', 3);
//    }
//
//    private static class letter extends Thread {
//        char c;
//        int position;
//        static int count = 0;
//
//        letter(char c, int position) {
//            this.c = c;
//            this.position = position;
//            start();
//        }
//
//        @Override
//        public void run() {
//            try {
//                for (int i = 0; i < 3; i++) print(c, position);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private synchronized static void print(char c, int pos) throws InterruptedException {
//            while (count % 3 != pos - 1) letter.class.wait();
//            System.out.print(c);
//            count++;
//            letter.class.notifyAll();
//        }
//    }
//}
