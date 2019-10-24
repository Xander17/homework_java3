package lesson3.readbook;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReadBook {

    private static final int CHARS_PER_PAGE = 1800;
    private static final int CHARS_PER_LINE = 200;

    public static void main(String[] args) {
        File file = new File(Paths.get("src/lesson3/readbook/books.txt").toString());
        int totalPages = (int) Math.ceil(file.length() / (1.0 * CHARS_PER_PAGE));
        int pageNum = 1;
        try (RandomAccessFile in = new RandomAccessFile(file, "r"); Scanner csl = new Scanner(System.in)) {
            getPage(in, pageNum);
            while (true) {
                System.out.println();
                System.out.println("--Страница " + pageNum + ". Всего страниц: " + totalPages + ". Введите номер страницы или выберите следующую '>' или предуыдущую '<': ");
                String pageStr = csl.next();
                if (pageStr.equalsIgnoreCase("exit")) break;
                if (pageStr.matches("^\\d+$")) {
                    pageNum = Integer.parseInt(pageStr);
                    if (pageNum > 0 && pageNum <= totalPages) getPage(in, pageNum);
                } else if (pageStr.equals(">") && pageNum < totalPages) getPage(in, ++pageNum);
                else if (pageStr.equals("<") && pageNum > 1) getPage(in, --pageNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void getPage(RandomAccessFile in, int page) throws IOException {
        in.seek(CHARS_PER_PAGE * (page - 1) + 1);
        byte[] b = new byte[CHARS_PER_PAGE];
        in.read(b, 0, CHARS_PER_PAGE);
        StringBuilder s = new StringBuilder(new String(b, "WINDOWS-1251"));

        cutLines(s, CHARS_PER_LINE);
        System.out.println(s.toString());
    }

    private static void cutLines(StringBuilder s, int length) {
        int i = 0;
        while (true) {
            int i2 = s.indexOf("\n", i);
            if (i2 < 0 && s.length() - i < length) break;
            while ((s.length() - i > length) && s.charAt(i + length) != ' ') i++;
            if (i2 - i > length || (i2 < 0 && s.length() - i > length)) {
                s.insert(i + length, "\r\n");
                i = i + length + 2;
            } else
                i = i2 + 1;
        }
    }
}
