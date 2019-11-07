package lesson8.seaports;

import lesson8.seaports.cargo.Cargo;

public class Seaport {

    private final static int PIECE_LOADTIME = 10;
    private final static int DOCKING_PAUSE = 500;

    private Cargo cargo;
    private String name;
    private boolean isLoading = false;

    public boolean getLoadingToken() {
        synchronized (Seaport.class) {
            if (!isLoading) {
                isLoading = true;
                return true;
            }
        }
        return false;
    }

    public Seaport(String name, Cargo cargo) {
        this.cargo = cargo;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Seaport(String name) {
        this(name, null);
    }

    public void loadShip(Ship ship) throws InterruptedException {
        System.out.println(String.format("Корабль '%s':\tпричалил в порт '%s'", ship.getShipName(), name));
        Thread.sleep(DOCKING_PAUSE);
        System.out.println(String.format("Порт '%s':\tначинается загрузка в корабль '%s' -> %s", name, ship.getShipName(), cargo.getType().NAME));
        int availableCargoAmount = Math.min(cargo.getAmount(), ship.getCargoCapacity());
        Thread.sleep(PIECE_LOADTIME * availableCargoAmount);
        cargo.decreaseAmount(availableCargoAmount);
        ship.setCargo(Cargo.getNewCargo(cargo.getType(),availableCargoAmount));
        System.out.println(String.format("Порт '%s':\tзагружено в корабль '%s' -> %dшт. %s", name, ship.getShipName(), availableCargoAmount, cargo.getType().NAME));
        System.out.println(String.format("Порт '%s':\tна складе осталось -> %dшт. %s", name, cargo.getAmount(), cargo.getType().NAME));
        Thread.sleep(DOCKING_PAUSE);
        System.out.println(String.format("Корабль '%s':\tотчалил от порта '%s'", ship.getShipName(), name));
        isLoading = false;
    }

    public void unloadShip(Ship ship) {

    }

    public Cargo getCargo() {
        return cargo;
    }
}
