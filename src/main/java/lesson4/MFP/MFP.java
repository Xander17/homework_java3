package lesson4.MFP;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class MFP {

    private final int TIME_BETWEEN_PRINT = 500;
    private final int TIME_BETWEEN_SCAN = 500;
    private final int TIME_ONE_PAGE_PRINT = 500;
    private final int TIME_ONE_PAGE_SCAN = 1000;
    private final int TIME_TO_SEND_SCAN_PAGE = 100;
    private final int TIME_TO_GET_PRINTED_DOC = 1000;

    private final int MAX_PAPER_LOAD = 100;
    private final int TIME_TO_LOAD_PAPER = 20000;

    private final PrinterUnit printer;
    private final ScannerUnit scanner;

    private final Vector<PaperDocument> printResult;
    private final Vector<EDocument> scanCache;

    private SimpleDateFormat dateFormat;

    public MFP() {
        printResult = new Vector<>();
        scanCache = new Vector<>();
        printer = new PrinterUnit();
        scanner = new ScannerUnit();
        dateFormat = new SimpleDateFormat("[HH:mm:ss]");
        System.out.println("Запущено МФУ");
    }

    public void sendToPrinter(EDocument doc, int copies) {
        new Thread(() -> {
            PaperDocument paperDocument;
            printer.addToQueue(doc, copies);
            try {
                synchronized (printResult) {
                    while ((paperDocument = lookForPrintedDoc(doc, copies)) == null) {
                        printResult.wait();
                    }
                    getPrintedDoc(paperDocument, copies);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getFromScanner(PaperDocument doc) {
        new Thread(() -> {
            EDocument eDocument;
            scanner.addToQueue(doc);
            try {
                synchronized (scanCache) {
                    while ((eDocument = lookForScannedDoc(doc)) == null) scanCache.wait();
                    sendEDoc(eDocument);
                }
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
                scanner.addToQueue(doc);
                synchronized (scanCache) {
                    while ((eDocument = lookForScannedDoc(doc)) == null) scanCache.wait();
                    removeScanFromCache(eDocument);
                }
                printMessage(String.format("Ксерокопируемый документ передан на принтер: %s %s", doc, getScanCacheInfo()), MessageType.IN, TaskType.SCAN);
                printer.addToQueue(eDocument, copies);
                synchronized (printResult) {
                    while ((paperDocument = lookForPrintedDoc(eDocument, copies)) == null) printResult.wait();
                    getCopiedDoc(paperDocument, copies);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class PrinterUnit extends Thread {
        private final Vector<EDocument> docList;
        private int paperLoad;

        public PrinterUnit() {
            docList = new Vector<>();
            paperLoad = MAX_PAPER_LOAD;
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
                }
                print(docList.get(0));
                Thread.sleep(TIME_BETWEEN_PRINT);
            }
        }

        private void print(EDocument doc) throws InterruptedException {
            printMessage("Отправлено на печать: " + doc, MessageType.IN, TaskType.PRINT);
            for (int i = 0; i < doc.getPages(); i++) {
                if (paperLoad <= 0) {
                    System.out.println("Закончилась бумага");
                    paperRequest();
                }
                Thread.sleep(TIME_ONE_PAGE_PRINT);
                paperLoad -= 1;
            }
            PaperDocument printed = doc.getPrintedCopy();
            synchronized (docList) {
                docList.remove(doc);
            }
            printMessage(String.format("Напечатано: %s %s", printed, getQueueInfo()), MessageType.OUT, TaskType.PRINT);
            synchronized (printResult) {
                printResult.add(printed);
                printResult.notifyAll();
            }
        }

        private void addToQueue(EDocument doc, int copies) {
            synchronized (docList) {
                for (int i = 0; i < copies; i++) docList.add(doc);
            }
            printMessage(String.format("Добавлено в очередь на печать: %s в %dэкз. %s", doc, copies, getQueueInfo()), MessageType.IN, TaskType.PRINT);
            synchronized (this) {
                notify();
            }
        }

        private String getQueueInfo() {
            return "Документов в очереди: " + docList.size();
        }

        private void paperRequest() throws InterruptedException {
            Thread.sleep(TIME_TO_LOAD_PAPER);
            paperLoad = MAX_PAPER_LOAD;
            System.out.println("Бумага загружена");
        }
    }

    private class ScannerUnit extends Thread {
        private final Vector<PaperDocument> docList;

        public ScannerUnit() {
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
            printMessage("Начато сканирование: " + doc, MessageType.IN, TaskType.SCAN);
            Thread.sleep(TIME_ONE_PAGE_SCAN * doc.getPages());
            EDocument scanned = doc.getScanCopy();
            synchronized (docList) {
                docList.remove(doc);
            }
            printMessage(String.format("Отсканировано: %s %s", scanned, getQueueInfo()), MessageType.OUT, TaskType.SCAN);
            synchronized (scanCache) {
                scanCache.add(scanned);
                scanCache.notifyAll();
            }
        }

        private void addToQueue(PaperDocument doc) {
            synchronized (docList) {
                docList.add(doc);
            }
            printMessage(String.format("Пользователь встал в очередь на сканирование: %s %s", doc, getQueueInfo()), MessageType.IN, TaskType.SCAN);
            synchronized (this) {
                notifyAll();
            }
        }

        private String getQueueInfo() {
            return "Пользователей в очереди: " + docList.size();
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

    private void getPaperDoc(PaperDocument doc) throws InterruptedException {
        Thread.sleep(TIME_TO_GET_PRINTED_DOC);
        for (int i = printResult.size() - 1; i >= 0; i--) {
            PaperDocument result = printResult.get(i);
            if (result.getName().equals(doc.getName())) {
                printResult.remove(result);
            }
        }
    }

    private void getPrintedDoc(PaperDocument doc, int copies) throws InterruptedException {
        getPaperDoc(doc);
        printMessage(String.format("Документ забрали из лотка: %s в %dэкз. %s", doc, copies, getPrintResultInfo()), MessageType.OUT, TaskType.PRINT);
    }

    private void getCopiedDoc(PaperDocument doc, int copies) throws InterruptedException {
        getPaperDoc(doc);
        printMessage(String.format("Отксеренный документ забрали из лотка: %s в %dэкз. %s", doc, copies, getPrintResultInfo()), MessageType.OUT, TaskType.PRINT);
    }

    private EDocument lookForScannedDoc(PaperDocument doc) {
        for (EDocument result : scanCache) {
            if (result.getName().equals(doc.getName())) return result;
        }
        return null;
    }

    private void sendEDoc(EDocument doc) throws InterruptedException {
        Thread.sleep(TIME_TO_SEND_SCAN_PAGE * doc.getPages());
        scanCache.remove(doc);
        printMessage(String.format("Документ отправлен пользователю: %s %s", doc, getScanCacheInfo()), MessageType.OUT, TaskType.SCAN);
    }

    private void removeScanFromCache(EDocument doc) {
        scanCache.remove(doc);
    }

    private String getPrintResultInfo() {
        return "Документов в лотке: " + printResult.size();
    }

    private String getScanCacheInfo() {
        return "Отсканированных документов в кэше: " + scanCache.size();
    }

    private String getTimeStamp() {
        return dateFormat.format(new Date());
    }

    private void printMessage(String str, MessageType type, TaskType task) {
        String typeStr;
        String taskStr;
        if (type == MessageType.IN) typeStr = "->";
        else typeStr = "<-";
        if (task == TaskType.PRINT) taskStr = "\t";
        else taskStr = "\t\t";
        System.out.println(String.format("%s %s%s%s", typeStr, getTimeStamp(), taskStr, str));
    }

    enum MessageType {IN, OUT}

    enum TaskType {PRINT, SCAN}
}

