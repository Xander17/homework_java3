package lesson1.flatlist;

import java.util.ArrayList;
import java.util.HashMap;

public class FlatMain {
    public static void main(String[] args) {
        ArrayList<Flatlist> list = new ArrayList<>();

        list.add(new Flatlist("Test1"));
        list.add(new Flatlist("Test1"));
        list.add(new Flatlist("Test1"));
        list.add(new Flatlist("Test2"));
        list.add(new Flatlist("Test2"));
        list.add(new Flatlist("Test3"));
        list.add(new Flatlist("Test3"));
        list.add(new Flatlist("Test4"));

        HashMap<String, ArrayList<Integer>> map = listToMap(list);

        System.out.println(map);
    }

    private static HashMap<String, ArrayList<Integer>> listToMap(ArrayList<Flatlist> list) {
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        list.forEach(entry -> map.computeIfAbsent(entry.getName(), k -> new ArrayList<>()).add(entry.getId()));
//            String k = entry.getName();
//            ArrayList<Integer> v = map.getOrDefault(k, new ArrayList<>());
//            v.add(entry.getId());
//            map.put(k, v);
        return map;
    }
}
