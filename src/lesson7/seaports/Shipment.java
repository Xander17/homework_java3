package lesson7.seaports;

import lesson7.seaports.cargo.Cargo;
import lesson7.seaports.cargo.CargoType;

import java.util.ArrayList;

public class Shipment {
    public static void main(String[] args) {
        new Shipment();
    }

    private ArrayList<Seaport> ports;
    private Seaport unloadPort;
    private final SeaChannel channel;
    private int availablePorts;

    private Shipment() {
        channel = new SeaChannel();

        ports = new ArrayList<>();
        ports.add(new Seaport("Сеул", Cargo.getNewCargo(CargoType.CLOTHES, 2700),this));
        ports.add(new Seaport("Роттердам", Cargo.getNewCargo(CargoType.FOOD, 5900),this));
        ports.add(new Seaport("Архангельск", Cargo.getNewCargo(CargoType.FUEL, 8500),this));
        availablePorts = ports.size();

        unloadPort = new Seaport("Нью-Йорк",this);

        new Ship("Резвый", 500,this);
        new Ship("Шустрый", 500,this);
        new Ship("Быстрый", 500,this);
    }

    public int getAvailablePortsCount() {
        return availablePorts;
    }

    public void setAvailablePortsCount(int availablePorts) {
        this.availablePorts = availablePorts;
    }

    public Seaport getAvailablePort() {
        boolean checkFill = true;
        while (checkFill) {
            checkFill = false;
            for (Seaport port : ports) {
                if (port.getCargo() == null) continue;
                checkFill = true;
                if (port.getLoadingToken()) {
                    return port;
                }
            }
        }
        return null;
    }

    public SeaChannel getChannel() {
        return channel;
    }

    public Seaport getUnloadPort() {
        return unloadPort;
    }
}
