package lesson7.seaports;

import lesson7.seaports.cargo.Cargo;

public class Ship extends Thread {
    private int cargoWeightCapacity;
    private Cargo cargo;
    private String name;
    private Shipment shipment;

    public Ship(String name, int cargoWeightCapacity, Cargo cargo) {
        this.cargoWeightCapacity = cargoWeightCapacity;
        this.cargo = cargo;
        this.name = name;
    }

    public Ship(String name, int cargoWeightCapacity) {
        this(name, cargoWeightCapacity, null);
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(String.format("Корабль '%s':\tожидает свободный порт загрузки", name));
            Seaport port = shipment.getAvailablePort();
            if (port == null) break;
            try {
                port.loadShip(this);
                shipment.getChannel().passThrough(this);
                shipment.getUnloadPort().unloadShip(this);
                if (shipment.getAvailablePortsCount() == 0) break;
                shipment.getChannel().passThrough(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(String.format("-> Корабль '%s':\tзакончил перевозку, т.к. все склады пусты", name));
    }

    public int getCargoWeightCapacity() {
        return cargoWeightCapacity;
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

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
}
