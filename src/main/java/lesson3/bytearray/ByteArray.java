package lesson3.bytearray;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

public class ByteArray {
    public static void main(String[] args) {
        new ByteArray();
    }

    private ByteArray() {
        File file = new File(Paths.get("src/lesson3/bytearray/file.txt").toString());
        byte[] bytes = new byte[512];
        try (FileInputStream in = new FileInputStream(file)) {
            while (in.read(bytes) != -1) {
                System.out.println(Arrays.toString(bytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
