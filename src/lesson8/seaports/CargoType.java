package lesson8.seaports;

enum CargoType {
    FOOD("Еда"),
    FUEL("Топливо"),
    CLOTHES("Одежда");

    public final String NAME;

    CargoType(String name) {
        this.NAME = name;
    }
}
