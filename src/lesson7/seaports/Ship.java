package lesson8.seaports;

import lesson8.seaports.cargo.Cargo;

public class Ship extends Thread {
    private int cargoCapacity;
    private Cargo cargo;
    private Shipment shipment;
    String name;

    public Ship(String name, int cargoCapacity, Cargo cargo, Shipment shipment) {
        this.cargoCapacity = cargoCapacity;
        this.cargo = cargo;
        this.name = name;
        this.shipment = shipment;
        start();
    }

    public Ship(String name, int cargoCapacity, Shipment shipment) {
        this(name, cargoCapacity, null, shipment);
    }

    @Override
    public void run() {
        while (shipment.isProcessing()) {
            Seaport port = shipment.getAvailablePort();
            if (port == null) break;
            try {
                port.loadShip(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public String getShipName() {
        return name;
    }
}
