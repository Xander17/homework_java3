package lesson7.seaports.cargo;

public class Food extends Cargo {
    Food(int amount) {
        super(CargoType.FOOD, 1,  amount);
    }
}
