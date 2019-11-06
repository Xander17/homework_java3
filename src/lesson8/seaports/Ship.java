package lesson8.seaports;

public class Ship {
    private int cargoCapacity;
    private Cargo cargo;
    String name;

    public Ship(String name, int cargoCapacity, Cargo cargo) {
        this.cargoCapacity = cargoCapacity;
        this.cargo = cargo;
    }

    public Ship(String name, int cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
        this.cargo = null;
    }

    public int getCargoCapacity() {
        return cargoCapacity;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}
