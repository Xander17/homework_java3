package lesson4.MFP;

public abstract class Document {
    private int pages;
    private String name;
    private String type;

    public Document(String type, String name, int pages) {
        this.pages = pages;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public String toString() {
        return type + " \"" + getName() + "\", " + getPages() + "стр.";
    }
}

class PaperDocument extends Document {
    public PaperDocument(String name, int pages) {
        super("Бумажный документ", name, pages);
    }
}

class EDocument extends Document {
    public EDocument(String name, int pages) {
        super("Электронный документ", name, pages);
    }
}
