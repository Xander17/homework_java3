package lesson7;

public class SpiralNumber {
    public static void main(String[] args) {
        new SpiralNumber(3, 3, StartPoint.TOP_LEFT, true);
        new SpiralNumber(5, 5, StartPoint.TOP_RIGHT, false);
        new SpiralNumber(7, 7, StartPoint.BOTTOM_LEFT, true);
        new SpiralNumber(12, 12, StartPoint.BOTTOM_RIGHT, false);
        new SpiralNumber(20, 20, StartPoint.TOP_LEFT, false);
    }

    enum StartPoint {TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT}

    private int[][] array;
    private int rows;
    private int cols;
    private int[] vector = new int[2];
    private int curVector;
    private int[] coords;
    private boolean clockwise;

    private SpiralNumber(int rows, int cols, StartPoint startPoint, boolean clockwise) {
        if (rows == 0 || cols == 0) return;
        this.rows = rows;
        this.cols = cols;
        this.clockwise = clockwise;
        setStartSettings(startPoint);
        array = getSpiralArray();
        printArray();
    }

    private void setStartSettings(StartPoint startPoint) {
        switch (startPoint) {
            case TOP_LEFT:
                coords = new int[]{0, 0};
                break;
            case TOP_RIGHT:
                coords = new int[]{0, cols - 1};
                break;
            case BOTTOM_RIGHT:
                coords = new int[]{rows - 1, cols - 1};
                break;
            case BOTTOM_LEFT:
                coords = new int[]{rows - 1, 0};
                break;
        }
        curVector = clockwise ? startPoint.ordinal() : 1 + startPoint.ordinal();
        setNewVector();
    }

    private int[][] getSpiralArray() {
        array = new int[rows][cols];
        int i = 1;
        while (i <= rows * cols) {
            array[coords[0]][coords[1]] = i;
            i++;
            if (!tryAddVector()) {
                curVector += clockwise ? 1 : -1;
                setNewVector();
            }
            addVector();
        }
        return array;
    }

    private void setNewVector() {
        vector[0] = (int) Math.sin(curVector * 0.5 * Math.PI);
        vector[1] = (int) Math.cos(curVector * 0.5 * Math.PI);
    }

    private boolean tryAddVector() {
        int newX = coords[0] + vector[0];
        int newY = coords[1] + vector[1];

        return newX < cols && newX >= 0 &&
                newY < rows && newY >= 0 &&
                array[newX][newY] == 0;
    }

    private void addVector() {
        coords[0] = coords[0] + vector[0];
        coords[1] = coords[1] + vector[1];
    }

    private void printArray() {
        System.out.println();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(array[i][j]);
                if (j != array[i].length - 1) System.out.print("\t");
            }
            System.out.println();
        }
    }
}
