package xyz.thekoc.sysalert;

import com.esotericsoftware.yamlbeans.YamlException;
import xyz.thekoc.sysalert.Sysalert;
import xyz.thekoc.sysalert.agent.PostAgentTest;
import xyz.thekoc.sysalert.alert.ConsoleAlerter;
import xyz.thekoc.sysalert.alert.PopupAlerter;
import xyz.thekoc.sysalert.conifg.Config;
import xyz.thekoc.sysalert.conifg.FieldMissingException;
import xyz.thekoc.sysalert.conifg.FieldValueException;
import xyz.thekoc.sysalert.conifg.NoSuchRuleException;
import xyz.thekoc.sysalert.rule.RuleType;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class SysalertTest {

    @org.junit.Test
    public void main() throws FileNotFoundException, YamlException, FieldMissingException, NoSuchRuleException, FieldValueException {
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

        new PostAgentTest().generateEvent(10000, 3000, 8, 100000);
        // TODO: parse command line arguments
        // TODO: add rules from `rule_folder`
        Config config = Config.getConfig();
        String rulePath = SysalertTest.class.getClassLoader().getResource("test_rule.yml").getPath();
        RuleType rule = config.addRule(rulePath);

        rule.addAlerter(new ConsoleAlerter());
        rule.addAlerter(new PopupAlerter());
        Sysalert s = new Sysalert(config.getHostname(), config.getPort(), config.getScheme());
        s.addRules(config.getRuleTypes());
        s.start();
    }
}