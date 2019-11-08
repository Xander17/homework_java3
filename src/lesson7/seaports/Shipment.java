package lesson7.seaports;

import lesson7.seaports.cargo.Cargo;
import lesson7.seaports.cargo.CargoType;

import java.util.ArrayList;
import java.util.Arrays;

public class Shipment {
    private final int SHIPS_LIMIT = 2;

    public static void main(String[] args) {
        Shipment shipment = new Shipment();
        shipment.setPorts(
                new Seaport("Кейптаун", Cargo.getNewCargo(CargoType.CLOTHES, 2700)),
                new Seaport("Роттердам", Cargo.getNewCargo(CargoType.FOOD, 5900)),
                new Seaport("Архангельск", Cargo.getNewCargo(CargoType.FUEL, 8500)));
        shipment.setShips(
                new Ship("Резвый", 500),
                new Ship("Шустрый", 500),
                new Ship("Юркий", 500),
                new Ship("Бегущий", 500),
                new Ship("Торнадо", 500),
                new Ship("Рьяный", 500),
                new Ship("Быстрый", 500)
        );
        shipment.setUnloadPort(new Seaport("Нью-Йорк"));
        shipment.start();
    }

    private ArrayList<Seaport> ports = null;
    private ArrayList<Ship> ships = null;
    private Seaport unloadPort;
    private final SeaChannel channel;
    private int availablePorts;

    private Shipment() {
        channel = new SeaChannel(SHIPS_LIMIT);
        ports = null;
        ships = null;
        unloadPort = null;
    }

    public void setPorts(Seaport... seaports) {
        ports = new ArrayList<>(Arrays.asList(seaports));
        ports.forEach((port) -> port.setShipment(this));
        availablePorts = ports.size();
    }

    public void setShips(Ship... ships) {
        this.ships = new ArrayList<>(Arrays.asList(ships));
        this.ships.forEach((ship) -> ship.setShipment(this));
    }

    public void setUnloadPort(Seaport port) {
        unloadPort = port;
        unloadPort.setShipment(this);
    }

    public void start() {
        if (ports == null || ports.size() == 0) {
            System.out.println("Не хватает портов с товаром");
            return;
        }
        if (ships == null || ships.size() == 0) {
            System.out.println("Не хватает кораблей");
            return;
        }
        if (unloadPort == null) {
            System.out.println("Нет порта выгрузки");
            return;
        }
        ships.forEach(Thread::start);
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
