package lesson1.flatlist;

public class Flatlist {
    private static int index = 1;
    private int id;
    private String name;

    Flatlist(String name) {
        this.id = Flatlist.index++;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
