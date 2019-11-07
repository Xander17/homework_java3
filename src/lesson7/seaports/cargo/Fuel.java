package lesson7.seaports.cargo;

public class Fuel extends Cargo {
    Fuel(int amount) {
        super(CargoType.FUEL, 1.5, amount);
    }
}
