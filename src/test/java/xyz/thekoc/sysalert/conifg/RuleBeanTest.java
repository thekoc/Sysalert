package xyz.thekoc.sysalert.conifg;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import static org.junit.Assert.*;

public class RuleBeanTest {
    @Test
    public void RuleBeanTest() throws FileNotFoundException, YamlException {
        Config config = Config.getConfig();
        String rulePath = RuleBeanTest.class.getClassLoader().getResource("test_rule.yml").getPath();
        YamlReader rawReader = new YamlReader(new FileReader(rulePath));
        RuleBean ruleBean = new RuleBean((Map) rawReader.read());
    }
}