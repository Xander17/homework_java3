package lesson8.seaports.cargo;

public class Clothes extends Cargo {
    Clothes(int amount) {
        super(CargoType.CLOTHES,0.8, amount);
    }
}
