package xyz.thekoc.sysalert;

import com.esotericsoftware.yamlbeans.YamlException;
import xyz.thekoc.sysalert.conifg.FieldMissingException;
import xyz.thekoc.sysalert.conifg.NoSuchRuleException;

import java.io.FileNotFoundException;


public class SysalertTest {

    @org.junit.Test
    public void start() {

    }

    @org.junit.Test
    public void main() throws FileNotFoundException, YamlException, FieldMissingException, NoSuchRuleException {
        new PostAgentTest().generateEvent(1000, 10, 3);
        new PostAgentTest().generateEvent(10000, 10, 4);
        new PostAgentTest().generateEvent(10000, 10, 5);
        Sysalert.main(null);

    }
}