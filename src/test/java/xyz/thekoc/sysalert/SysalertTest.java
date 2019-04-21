package xyz.thekoc.sysalert;

import com.esotericsoftware.yamlbeans.YamlException;
import xyz.thekoc.sysalert.Sysalert;
import xyz.thekoc.sysalert.agent.PostAgentTest;
import xyz.thekoc.sysalert.conifg.FieldMissingException;
import xyz.thekoc.sysalert.conifg.NoSuchRuleException;

import java.io.FileNotFoundException;


public class SysalertTest {

    @org.junit.Test
    public void start() {

    }

    @org.junit.Test
    public void main() throws FileNotFoundException, YamlException, FieldMissingException, NoSuchRuleException {
        new PostAgentTest().generateEvent(10000, 88, 4);
        new PostAgentTest().generateEvent(10000, 99, 4, 4000);
        new PostAgentTest().generateEvent(10000, 300, 4, 3000);

        new PostAgentTest().generateEvent(10000, 80, 5, 5000);
        new PostAgentTest().generateEvent(10000, 100, 5);
        new PostAgentTest().generateEvent(10000, 40, 5, 5000);

        new PostAgentTest().generateEvent(10000, 188, 3);
        new PostAgentTest().generateEvent(10000, 244, 3, 10000);
        new PostAgentTest().generateEvent(10000, 100, 3, 15000);
        new PostAgentTest().generateEvent(10000, 3000, 3, 90000);
        Sysalert.main(null);

    }
}