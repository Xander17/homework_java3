package lesson8.seaports;

import lesson8.seaports.cargo.*;

import java.util.ArrayList;

public class Shipment {
    private boolean isProcessing = true;

    private ArrayList<Seaport> ports;

    public static void main(String[] args) {
        new Shipment();
    }

    private Shipment() {
        ports = new ArrayList<>();
        ports.add(new Seaport("Сеул", Cargo.getNewCargo(CargoType.CLOTHES, 2700)));
        ports.add(new Seaport("Роттердам", Cargo.getNewCargo(CargoType.FOOD, 5900)));
        ports.add(new Seaport("Архангельск", Cargo.getNewCargo(CargoType.FUEL, 8500)));

        new Ship("Резвый", 500, this);
        new Ship("Шустрый", 500, this);
        new Ship("Быстрый", 500, this);
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void setProcessing(boolean processing) {
        isProcessing = processing;
    }

    public ArrayList<Seaport> getPorts() {
        return ports;
    }

    public Seaport getAvailablePort() {
        boolean checkFill = true;
        while (isProcessing && checkFill) {
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
}
