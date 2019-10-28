package lesson4.MFP;

import java.util.Vector;

public class MFP {

    private final int TIME_BETWEEN_PRINT = 500;
    private final int TIME_BETWEEN_SCAN = 2000;
    private final int TIME_ONE_PAGE_PRINT = 500;
    private final int TIME_ONE_PAGE_SCAN = 500;
    private final int TIME_TO_SEND_SCAN = 100;
    private final int TIME_TO_GET_PRINTED_DOC = 1000;

    private final Printer printer;
    private final Scanner scanner;

    private Vector<PaperDocument> printResult;
    private Vector<EDocument> scanCache;

    public MFP() {
        printResult = new Vector<>();
        scanCache = new Vector<>();
        printer = new Printer();
        scanner = new Scanner();
        System.out.println("Запущено МФУ");
    }

    public void sendToPrinter(EDocument doc, int copies) {
        new Thread(() -> {
            PaperDocument paperDocument;
            printer.addToList(doc, copies);
            try {
                synchronized (printer) {
                    while ((paperDocument = lookForPrintedDoc(doc, copies)) == null) printer.wait();
                }
                getPrintedDoc(paperDocument, copies);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getFromScanner(PaperDocument doc) {
        new Thread(() -> {
            EDocument eDocument;
            scanner.addToList(doc);
            try {
                synchronized (scanner) {
                    while ((eDocument = lookForScannedDoc(doc)) == null) scanner.wait();
                }
                sendEDoc(eDocument);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void makeCopies(PaperDocument doc, int copies) {
        new Thread(() -> {
            EDocument eDocument;
            PaperDocument paperDocument;
            try {
                scanner.addToList(doc);
                synchronized (scanner) {
                    while ((eDocument = lookForScannedDoc(doc)) == null) scanner.wait();
                }
                printer.addToList(eDocument, copies);
                synchronized (printer) {
                    while ((paperDocument = lookForPrintedDoc(eDocument, copies)) == null) printer.wait();
                }
                getCopiedDoc(paperDocument, copies);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class Printer extends Thread {
        private Vector<EDocument> docList;

        public Printer() {
            docList = new Vector<>();
            start();
        }

        @Override
        public void run() {
            try {
                startPrintProcess();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void startPrintProcess() throws InterruptedException {
            while (true) {
                synchronized (this) {
                    while (docList.size() == 0) wait();
                    print(docList.get(0));
                }
                Thread.sleep(TIME_BETWEEN_PRINT);
            }
        }

        private synchronized void print(EDocument doc) throws InterruptedException {
            System.out.println("Отправлено на печать: " + doc);
            Thread.sleep(TIME_ONE_PAGE_PRINT * doc.getPages());
            PaperDocument printed = new PaperDocument(doc.getName(), doc.getPages());
            printResult.add(printed);
            synchronized (docList) {
                docList.remove(doc);
            }
            System.out.println("Напечатано: " + printed);
            notifyAll();
        }

        private void addToList(EDocument doc, int copies) {
            synchronized (docList) {
                for (int i = 0; i < copies; i++) docList.add(doc);
            }
            System.out.println("Добавлено в очередь на печать: " + doc + " в " + copies + "экз.");
            synchronized (this) {
                notifyAll();
            }
        }
    }

    private class Scanner extends Thread {
        private Vector<PaperDocument> docList;

        public Scanner() {
            docList = new Vector<>();
            start();
        }

        @Override
        public void run() {
            try {
                startScanProcess();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void startScanProcess() throws InterruptedException {
            while (true) {
                synchronized (this) {
                    while (docList.size() == 0) wait();
                    scan(docList.get(0));
                }
                Thread.sleep(TIME_BETWEEN_SCAN);
            }
        }

        private void scan(PaperDocument doc) throws InterruptedException {
            System.out.println("\tНачато сканирование: " + doc);
            Thread.sleep(TIME_ONE_PAGE_SCAN * doc.getPages());
            EDocument scanned = new EDocument(doc.getName(), doc.getPages());
            scanCache.add(scanned);
            synchronized (docList) {
                docList.remove(doc);
            }
            System.out.println("\tОтсканировано: " + scanned);
            notifyAll();
        }

        private void addToList(PaperDocument doc) {
            synchronized (docList) {
                docList.add(doc);
            }
            System.out.println("\tДобавлено в очередь на сканирование: " + doc);
            synchronized (this) {
                notifyAll();
            }
        }
    }

    private PaperDocument lookForPrintedDoc(EDocument doc, int copies) {
        int copiesPrinted = 0;
        PaperDocument paperDocument = null;
        for (PaperDocument result : printResult) {
            if (result.getName().equals(doc.getName())) {
                copiesPrinted++;
                if (paperDocument == null) paperDocument = result;
            }
        }
        if (copiesPrinted == copies) return paperDocument;
        else return null;
    }

    private synchronized void getPaperDoc(PaperDocument doc, int copies) throws InterruptedException {
        Thread.sleep(TIME_TO_GET_PRINTED_DOC);
        for (int i = printResult.size() - 1; i >= 0; i--) {
            PaperDocument result = printResult.get(i);
            if (result.getName().equals(doc.getName())) {
                printResult.remove(result);
            }
        }
    }

    private void getPrintedDoc(PaperDocument doc, int copies) throws InterruptedException {
        getPaperDoc(doc, copies);
        System.out.println("Документ забрали из лотка: " + doc + " в " + copies + "экз.");
    }

    private void getCopiedDoc(PaperDocument doc, int copies) throws InterruptedException {
        getPaperDoc(doc, copies);
        System.out.println("Отксеренный документ забрали из лотка: " + doc + " в " + copies + "экз.");
    }

    private EDocument lookForScannedDoc(PaperDocument doc) {
        for (EDocument result : scanCache) {
            if (result.getName().equals(doc.getName())) return result;
        }
        return null;
    }

    private synchronized void sendEDoc(EDocument doc) throws InterruptedException {
        Thread.sleep(TIME_TO_SEND_SCAN);
        scanCache.remove(doc);
        System.out.println("\tДокумент отправлен пользователю: " + doc);
    }
}

