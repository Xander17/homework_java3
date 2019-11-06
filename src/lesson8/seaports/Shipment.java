package lesson8.seaports;

import java.util.ArrayList;

public class Shipment {
    ArrayList<Seaport> ports;
    ArrayList<Ship> ships;

    public static void main(String[] args) {
        new Shipment();
    }

    private Shipment() {
        ports = new ArrayList<>();
        ports.add(new Seaport(new Clothes(2700)));
        ports.add(new Seaport(new Food(5900)));
        ports.add(new Seaport(new Fuel(8500)));

        ships = new ArrayList<>();
        ships.add(new Ship("Резвый", 500));
        ships.add(new Ship("Шустрый", 500));
        ships.add(new Ship("Быстрый", 500));
    }
}
