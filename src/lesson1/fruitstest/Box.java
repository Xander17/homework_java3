package lesson1.fruitstest;

import java.util.ArrayList;

class Box<T extends Fruit> {
    private ArrayList<T> fruits;

    Box() {
        fruits = new ArrayList<>();
    }

    void addFruit(T fruit) {
        fruits.add(fruit);
    }

    private float getWeight() {
        if (fruits.size() == 0) return 0;
        return fruits.size() * fruits.get(0).getWeight();
    }

    boolean compare(Box<?> anotherBox) {
        return this.getWeight() == anotherBox.getWeight();
    }

    void displaceTo(Box<T> boxMoveTo) {
        fruits.forEach(boxMoveTo::addFruit);
        fruits.clear();
    }

    void print() {
        if (fruits.size() == 0) System.out.println("Пусто");
        else System.out.println(fruits.get(0).getClass().getSimpleName() + "\t" + fruits.size() + "шт\tвес - " + getWeight());
    }
}
