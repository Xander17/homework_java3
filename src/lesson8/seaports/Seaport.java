package lesson8.seaports;

public class Seaport {
    private Cargo cargo;

    public Seaport(Cargo cargo) {
        this.cargo = cargo;
    }

    public Seaport() {
        this.cargo = null;
    }

    public void loadShip(Ship ship) {

    }

    public void unloadShip(Ship ship) {

    }

    public Cargo getCargo() {
        return cargo;
    }
}
