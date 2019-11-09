package lesson7.seaports.cargo;

public abstract class Cargo {
    private double weightFactor;
    private int amount;
    private CargoType type;

    public Cargo(CargoType type, double weightFactor, int amount) {
        this.type = type;
        this.weightFactor = weightFactor;
        this.amount = amount;
    }

    public synchronized int decreaseAmount(int delta) {
        amount -= delta;
        return amount;
    }

    public synchronized int getAmount() {
        return amount;
    }

    public int getCountInWeight(int amount) {
        return (int) (amount / weightFactor);
    }

    public CargoType getType() {
        return type;
    }

    public static Cargo getNewCargo(CargoType type, int amount) {
        switch (type) {
            case FOOD:
                return new Food(amount);
            case FUEL:
                return new Fuel(amount);
            case CLOTHES:
                return new Clothes(amount);
        }
        return null;
    }

}
