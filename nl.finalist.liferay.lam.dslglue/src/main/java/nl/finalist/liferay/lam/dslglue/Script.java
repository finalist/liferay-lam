package nl.finalist.liferay.lam.dslglue;

import java.io.Reader;

public class Script {

    private String name;
    private Reader reader;


    public Script(String name, Reader reader) {
        this.name = name;
        this.reader = reader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }
}
