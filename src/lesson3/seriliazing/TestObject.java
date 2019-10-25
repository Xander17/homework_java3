package lesson3.seriliazing;

import java.io.Serializable;
import java.util.ArrayList;

public class TestObject implements Serializable {

    private int field1;
    private String field2;
    private ArrayList<String> list;

    public TestObject(int field1, String field2, ArrayList<String> list) {
        this.field1 = field1;
        this.field2 = field2;
        this.list = list;
    }

    public int getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public ArrayList<String> getList() {
        return new ArrayList<>(list);
    }
}
