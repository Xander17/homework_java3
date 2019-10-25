package lesson3.readbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReadBook {
    public static void main(String[] args) {
        try {
            new ReadBook();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int CHARS_PER_PAGE = 1800;
    private static final int CHARS_PER_LINE = 200;

    private ReadBook() throws IOException {
        File file = new File(Paths.get("src/lesson3/readbook/books.txt").toString());
        RandomAccessFile in = new RandomAccessFile(file, "r");
        Scanner console = new Scanner(System.in);
        int totalPages = (int) Math.ceil(file.length() / (1.0 * CHARS_PER_PAGE));
        int pageNum = 1;

        getPage(in, pageNum);
        while (true) {
            System.out.println("--Страница " + pageNum + ". Всего страниц: " + totalPages + ". Введите номер страницы или выберите следующую '>' или предуыдущую '<': ");
            String pageStr = console.next();
            if (pageStr.equalsIgnoreCase("exit")) break;
            if (pageStr.matches("^\\d+$")) {
                int newPage = Integer.parseInt(pageStr);
                if (newPage > 0 && newPage <= totalPages) {
                    pageNum = newPage;
                    getPage(in, pageNum);
                }
            } else if (pageStr.equals(">") && pageNum < totalPages) getPage(in, ++pageNum);
            else if (pageStr.equals("<") && pageNum > 1) getPage(in, --pageNum);
        }
        in.close();
        console.close();
    }

    private void getPage(RandomAccessFile in, int page) throws IOException {
        in.seek(CHARS_PER_PAGE * (page - 1));
        byte[] b = new byte[CHARS_PER_PAGE];
        in.read(b, 0, CHARS_PER_PAGE);
        StringBuilder s = new StringBuilder(new String(b, "WINDOWS-1251"));

        cutLines(s, CHARS_PER_LINE);
        System.out.println(s.toString());
        System.out.println();
    }

    private void cutLines(StringBuilder s, int length) {
        int i = 0;
        while (true) {
            int i2 = s.indexOf("\n", i);
            if (i2 < 0 && s.length() - i <= length) break;
            if (i2 - i > length || (i2 < 0 && s.length() - i > length)) {
                while ((s.length() - i > length) && s.charAt(i + length) != ' ') i++;
                s.insert(i + length, "\r\n");
                i = i + length + 1;
            } else
                i = i2 + 1;
        }
    }
}
