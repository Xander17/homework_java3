import java.util.Scanner;

public class task1 {
    //задание 1
    public static void main(String[] args) {
        //задание 2
        byte byteType = 5;
        short shortType = 60;
        int intType = 10000;
        long longType = 100000L;
        float floatType = 3.14f;
        double doubleType = 50.66666;
        char charType = 'A';
        String strType = "String";

        Scanner userInput = new Scanner(System.in);
        Scanner userInput2 = new Scanner(System.in);
        float a, b, c, d;
        int e;
        String str;

        //задание 3
        a = consoleInputFloat(userInput, "Введите число a: ");
        b = consoleInputFloat(userInput, "Введите число b: ");
        c = consoleInputFloat(userInput, "Введите число c: ");
        d = consoleInputFloat(userInput, "Введите число d: ");
        System.out.println("Значение выражения " + a + "*(" + b + "+(" + c + "/" + d + ")) = " + calcExpression(a, b, c, d));
        System.out.println();

        //задание 4
        a = consoleInputFloat(userInput, "Введите число a: ");
        b = consoleInputFloat(userInput, "Введите число b: ");
        System.out.println("Сумма " + a + " и " + b + (checkSum20(a, b) ? " между 10 и 20" : " меньше 10 или больше 20"));
        System.out.println();

        //задание 5
        e = consoleInputInt(userInput, "Введите целое число: ");
        writeSign(e);
        System.out.println();

        //задание 6
        e = consoleInputInt(userInput, "Введите целое число: ");
        System.out.println("Число " + e + (checkSign(e) ? " отрицательное" : " положительное"));
        System.out.println();

        //задание 7
        str = consoleInputStr(userInput2, "Введите имя: ");
        writeHello(str);
        System.out.println();

        //задание 8
        e = consoleInputInt(userInput, "Введите год: ");
        checkLeapYear(e);
    }

    static float calcExpression (float a, float b, float c, float d) {
        return a * (b + (c / d));
    }

    static boolean checkSum20(float a, float b) {
        return a + b >= 10 && a + b <= 20;
    }

    static void writeSign(int a) {
        if (a >= 0)
            System.out.println("Число " + a + " положительное");
        else
            System.out.println("Число " + a + " отрицательное");
    }

    static boolean checkSign(int a) {
        return a < 0;
    }

    static void writeHello(String str) {
        System.out.println("Привет, " + str);
    }

    static void checkLeapYear(int year) {
        if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0))
            System.out.println(year + " год високосный");
        else
            System.out.println(year + " год невисокосный");
    }

    static float consoleInputFloat(Scanner userInput, String str) {
        System.out.print(str);
        return userInput.nextFloat();
    }

    static int consoleInputInt(Scanner userInput, String str) {
        System.out.print(str);
        return userInput.nextInt();
    }

    static String consoleInputStr(Scanner userInput, String str) {
        System.out.print(str);
        return userInput.nextLine();
    }
}
