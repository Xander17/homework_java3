package lesson8.seaports.cargo;

public class Fuel extends Cargo {
    Fuel(int amount) {
        super(CargoType.FUEL, 2, amount);
    }
}
