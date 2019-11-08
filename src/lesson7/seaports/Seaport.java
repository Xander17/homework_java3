package lesson7.seaports;

import lesson7.seaports.cargo.Cargo;

import java.util.concurrent.locks.ReentrantLock;

public class Seaport {

    private final static int PIECE_LOADTIME = 10;
    private final static int DOCKING_PAUSE = 500;

    private Cargo cargo;
    private String name;
    private boolean isLoading = false;
    private Shipment shipment;

    public Seaport(String name, Cargo cargo) {
        this.cargo = cargo;
        this.name = name;
    }

    public Seaport(String name) {
        this(name, null);
    }

    public boolean getLoadingToken() {
        synchronized (Seaport.class) {
            if (!isLoading) {
                isLoading = true;
                return true;
            }
        }
        return false;
    }

    public void loadShip(Ship ship) throws InterruptedException {
        if (cargo == null) return;

        String shipName = ship.getShipName();
        String cargoName = cargo.getType().NAME;

        System.out.println(String.format("Корабль '%s':\tпричалил в порт '%s'", shipName, name));
        Thread.sleep(DOCKING_PAUSE);
        System.out.println(String.format("Порт '%s':\tначинается загрузка в корабль '%s' -> %s", name, shipName, cargoName));

        int availableCargoAmount = Math.min(cargo.getAmount(), cargo.getCountInVolume(ship.getCargoWeightCapacity()));
        Thread.sleep(PIECE_LOADTIME * availableCargoAmount);
        ship.setCargo(Cargo.getNewCargo(cargo.getType(), availableCargoAmount));
        cargoUnloadFromWarehouse(availableCargoAmount);

        System.out.println(String.format("Порт '%s':\tзагружено в корабль '%s' -> %s %dшт.", name, shipName, cargoName, availableCargoAmount));
        if (cargo == null) System.out.println(String.format("-> Порт '%s':\tсклад пуст", name));
        else
            System.out.println(String.format("Порт '%s':\tна складе осталось -> %s %dшт.", name, cargoName, cargo.getAmount()));
        Thread.sleep(DOCKING_PAUSE);
        System.out.println(String.format("Корабль '%s':\tотчалил от порта '%s'", shipName, name));
        isLoading = false;
    }

    public synchronized void unloadShip(Ship ship) throws InterruptedException {
        if (ship.getCargo() == null) return;
        String shipName = ship.getShipName();
        String cargoName = ship.getCargo().getType().NAME;

        System.out.println(String.format("Корабль '%s':\tпричалил в порт '%s'", shipName, name));
        Thread.sleep(DOCKING_PAUSE);
        int cargoAmount = ship.getCargo().getAmount();
        System.out.println(String.format("Порт '%s':\tначинается разгрузка корабля '%s' -> %s %dшт.", name, shipName, cargoName, ship.getCargo().getAmount()));

        Thread.sleep(PIECE_LOADTIME * cargoAmount);
        ship.setCargo(null);

        System.out.println(String.format("Порт '%s':\tкорабль '%s' полностью разгружен", name, shipName));
        Thread.sleep(DOCKING_PAUSE);
        System.out.println(String.format("Корабль '%s':\tотчалил от порта '%s'", shipName, name));
    }

    private void cargoUnloadFromWarehouse(int amount) {
        if (cargo.decreaseAmount(amount) == 0) {
            cargo = null;
            shipment.setAvailablePortsCount(shipment.getAvailablePortsCount() - 1);
        }
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
}
