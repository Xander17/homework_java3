package lesson7.seaports.cargo;

public enum CargoType {
    FOOD("Еда"),
    FUEL("Топливо"),
    CLOTHES("Одежда");

    public final String NAME;

    CargoType(String name) {
        this.NAME = name;
    }
}
