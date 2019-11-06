package lesson8.seaports;

public abstract class Cargo {
    private double weightFactor;
    private int amount;
    private CargoType type;

    public Cargo(CargoType type, double weightFactor, int amount) {
        this.type = type;
        this.weightFactor = weightFactor;
        this.amount = amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public double getWeightFactor() {
        return weightFactor;
    }

    public CargoType getType() {
        return type;
    }

}
