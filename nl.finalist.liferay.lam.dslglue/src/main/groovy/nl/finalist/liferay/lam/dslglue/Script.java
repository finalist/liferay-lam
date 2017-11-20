package nl.finalist.liferay.lam.dslglue;

import java.io.Reader;
/**
 * Script holds all script data that is required for a flyway migration registration
 * @author danielle.ardon
 *
 */
public class Script {

    private String name;
    private Reader reader;
    private Integer checksum;


    public Script(String name, Reader reader, int checksum) {
        this.name = name;
        this.reader = reader;
        this.checksum = checksum;
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

    public Integer getChecksum() {
        return checksum;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }
}
