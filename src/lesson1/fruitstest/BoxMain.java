package lesson1.fruitstest;

public class BoxMain {

    public static void main(String[] args) {
        Box<Apple> boxApple1 = new Box<>();
        Box<Apple> boxApple2 = new Box<>();
        Box<Orange> boxOrange = new Box<>();

        for (int i = 0; i < 15; i++) boxApple1.addFruit(new Apple());
        for (int i = 0; i < 10; i++) boxApple2.addFruit(new Apple());
        for (int i = 0; i < 10; i++) boxOrange.addFruit(new Orange());
        System.out.print("boxApple1:\t");
        boxApple1.print();
        System.out.print("boxApple2:\t");
        boxApple2.print();
        System.out.print("boxOrange:\t");
        boxOrange.print();

        System.out.println("Сравниваем boxApple1 и boxApple2:\t" + boxApple1.compare(boxApple2));
        System.out.println("Сравниваем boxApple1 и boxOrange:\t" + boxApple1.compare(boxOrange));
        System.out.println("Сравниваем boxApple2 и boxOrange:\t" + boxApple2.compare(boxOrange));

        System.out.println("Пересыпаем из boxApple1 в boxApple2");
        boxApple1.displaceTo(boxApple2);
        System.out.print("boxApple1:\t");
        boxApple1.print();
        System.out.print("boxApple2:\t");
        boxApple2.print();
    }
}
