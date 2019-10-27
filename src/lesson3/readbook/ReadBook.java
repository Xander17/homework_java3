package lesson3.readbook;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadBook {
    public static void main(String[] args) {
        try {
            new ReadBook("src/lesson3/readbook/books.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int CHARS_PER_PAGE = 1800;
    private static final int CHARS_PER_LINE = 200;

    private ArrayList<Integer> pagesStartBytes;
    private RandomAccessFile in;

    private ReadBook(String filepath) throws IOException {
        File file = new File(Paths.get(filepath).toString());
        in = new RandomAccessFile(file, "r");
        Scanner console = new Scanner(System.in);
        pagesStartBytes = parsePages();
        int totalPages = pagesStartBytes.size() - 1;
        int pageNum = 1;
        getPage(pageNum);
        while (true) {
            System.out.println("--Страница " + pageNum + ". Всего страниц: " + totalPages + ". Введите номер страницы или выберите следующую '>' или предыдущую '<': ");
            String pageStr = console.next();
            if (pageStr.equalsIgnoreCase("exit")) break;
            if (pageStr.matches("^\\d+$")) {
                int newPage = Integer.parseInt(pageStr);
                if (newPage > 0 && newPage <= totalPages) {
                    pageNum = newPage;
                    getPage(pageNum);
                }
            } else if (pageStr.equals(">") && pageNum < totalPages) getPage(++pageNum);
            else if (pageStr.equals("<") && pageNum > 1) getPage(--pageNum);
        }
        in.close();
        console.close();
    }

    private ArrayList<Integer> parsePages() throws IOException {
        ArrayList<Integer> result = new ArrayList<>();
        List<Byte> bytesForString = new ArrayList<>();
        byte[] b = new byte[500];
        int pos = CHARS_PER_PAGE;
        int abspos = 0;
        StringBuilder s = new StringBuilder();
        in.seek(0);
        result.add(0);
        while (in.read(b) > 0) {
            bytesForString.addAll(getByteList(b));
            s = new StringBuilder(getStringFromByteList(bytesForString));
            if (s.length() >= CHARS_PER_PAGE) {
                while (pos < s.length() && (s.charAt(pos) != ' ' && s.charAt(pos) != '\n')) pos++;
                s.setLength(pos);
                abspos += s.toString().getBytes().length;
                result.add(abspos);
                in.seek(abspos);
                bytesForString.clear();
                pos = CHARS_PER_PAGE;
            }
        }
        abspos += s.toString().getBytes().length;
        result.add(abspos);
        return result;
    }

    private List<Byte> getByteList(byte[] b) {
        List<Byte> result = new ArrayList<>();
        for (byte value : b) {
            result.add(value);
        }
        return result;
    }

    private String getStringFromByteList(List<Byte> b) {
        byte[] bytes = new byte[b.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = b.get(i);
        }
        return new String(bytes);
    }

    private void getPage(int page) throws IOException {
        in.seek(pagesStartBytes.get(page - 1));
        byte[] b = new byte[pagesStartBytes.get(page) - pagesStartBytes.get(page - 1)];
        in.read(b);
        StringBuilder s = new StringBuilder(new String(b));

        cutLines(s);
        System.out.println(s.toString());
        System.out.println();
    }

    private void cutLines(StringBuilder s) {
        int i = 0;
        int length = CHARS_PER_LINE;
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
