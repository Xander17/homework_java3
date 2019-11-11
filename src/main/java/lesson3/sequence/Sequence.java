package lesson3.sequence;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Sequence {
    public static void main(String[] args) {
        try {
            new Sequence();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Sequence() throws IOException {
        byte[] bytes = new byte[128];

        ArrayList<InputStream> streams = new ArrayList<>();
        streams.add(new FileInputStream(Paths.get("src/lesson3/sequence/1.txt").toString()));
        streams.add(new FileInputStream(Paths.get("src/lesson3/sequence/2.txt").toString()));
        streams.add(new FileInputStream(Paths.get("src/lesson3/sequence/3.txt").toString()));
        streams.add(new FileInputStream(Paths.get("src/lesson3/sequence/4.txt").toString()));
        streams.add(new FileInputStream(Paths.get("src/lesson3/sequence/5.txt").toString()));

        SequenceInputStream in = new SequenceInputStream(Collections.enumeration(streams));
        FileOutputStream out = new FileOutputStream(Paths.get("src/lesson3/sequence/out.txt").toString(), false);

        while (in.read(bytes) != -1) {
            String s = new String(bytes, "WINDOWS-1251").replace("\0", "");
            if (in.available() == 0) s += "\n";
            out.write(s.getBytes());
            Arrays.fill(bytes, (byte) 0);
        }
        in.close();
        out.close();
    }
}
