package xyz.thekoc.sysalert;

import com.esotericsoftware.yamlbeans.YamlException;
import xyz.thekoc.sysalert.conifg.FieldMissingException;

import java.io.FileNotFoundException;


public class SysalertTest {

    @org.junit.Test
    public void start() {

    }

    @org.junit.Test
    public void main() throws FileNotFoundException, YamlException, FieldMissingException {

        Sysalert.main(null);

    }
}