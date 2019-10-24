package lesson3.reversereading;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;

public class ReverseReading {
    public static void main(String[] args) {
        File file = new File(Paths.get("src/lesson3/reversereading/burns.txt").toString());
        StringBuilder s = new StringBuilder("\n");
        char c;
        try (RandomAccessFile in = new RandomAccessFile(file, "r")) {
            for (long pos = file.length() - 1; pos >= 0; pos--) {
                in.seek(pos);
                c = (char) in.read();
                if (c == '\n') {
                    System.out.print(s.toString());
                    s = new StringBuilder();
                }
                s.insert(0, c);
            }
            System.out.println(s.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
