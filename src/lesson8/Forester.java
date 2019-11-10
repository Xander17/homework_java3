package lesson8;

import java.util.HashMap;
import java.util.Map;

public class Forester {
    public static void main(String[] args) {
        Forester forester = new Forester();
        Forest forest = new Forest(20, 20);
        forester.countTrees(forest);
    }

    private void countTrees(Forest forest) {
        TreeType[][] trees = forest.getForest();

        Map<TreeType, Integer> map = new HashMap<>();

        for (TreeType[] treeRow : trees) {
            for (TreeType tree : treeRow) {
                map.put(tree, map.getOrDefault(tree, 0) + 1);
            }
        }
        map.forEach((treeType, count) -> System.out.println(treeType.NAME + " - " + count + "шт."));
    }
}

class Forest {
    private TreeType[][] forest;

    Forest(int width, int height) {
        forest = new TreeType[height][width];
        TreeType[] types = TreeType.values();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                forest[i][j] = types[(int) (Math.random() * types.length)];
            }
        }
    }

    TreeType[][] getForest() {
        return forest;
    }
}

enum TreeType {
    SPRUCE("Ель"),
    PINE("Сосна"),
    FIR("Пихта"),
    NUT_PINE("Кедр"),
    LARIX("Лиственница"),
    CADE("Можжевельник"),
    YEW("Тисс"),
    THUJA("Туя");

    final String NAME;

    TreeType(String name) {
        this.NAME = name;
    }
}
