package xyz.thekoc.sysalert.rule;

import com.esotericsoftware.yamlbeans.YamlException;
import org.joda.time.Period;
import org.junit.Test;
import xyz.thekoc.sysalert.RuleHit;
import xyz.thekoc.sysalert.Sysalert;
import xyz.thekoc.sysalert.SysalertTest;
import xyz.thekoc.sysalert.agent.PostAgentTest;
import xyz.thekoc.sysalert.alert.Alerter;
import xyz.thekoc.sysalert.conifg.Config;
import xyz.thekoc.sysalert.conifg.FieldMissingException;
import xyz.thekoc.sysalert.conifg.FieldValueException;
import xyz.thekoc.sysalert.conifg.NoSuchRuleException;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;


public class SequenceRuleTest {

    @Test
    public void addMatchedEvents() throws NoSuchFileException, FieldValueException, FileNotFoundException, YamlException, NoSuchRuleException, FieldMissingException, InterruptedException {
        PostAgentTest postAgentTest = new PostAgentTest();

        URL url = getClass().getResource("/sequence_test.yml");
        if (url == null) {
            throw new NoSuchFileException("sequence_test.yaml");
        }
        String path = url.getPath();

        Config config = Config.getConfig();
        Sysalert sysalert = new Sysalert(config.getHostname(), config.getPort(), config.getScheme());
        RuleType sequenceRule = config.addRule(path);
        sysalert.addRule(sequenceRule);
        final boolean[] hitFlag = {false};
        sequenceRule.addAlerter(new Alerter() {
            @Override
            public void alert(RuleHit ruleHit) {
                hitFlag[0] = true;
            }
        });

        sysalert.init();
        Thread.sleep(1000);
        postAgentTest.postData(1, "1");
        Thread.sleep(1000);
        sysalert.runAllRules();
        assert !hitFlag[0];
        postAgentTest.postData(2, "2");
        Thread.sleep(1000);
        sysalert.runAllRules();
        assert !hitFlag[0];
        postAgentTest.postData(2, "3");
        Thread.sleep(1000);
        sysalert.runAllRules();
        assert !hitFlag[0];
        postAgentTest.postData(2, "4");
        Thread.sleep(1000);
        sysalert.runAllRules();
        assert !hitFlag[0];
        postAgentTest.postData(1, "5");
        Thread.sleep(1000);
        sysalert.runAllRules();
        assert !hitFlag[0];
        postAgentTest.postData(3, "6");
        Thread.sleep(1000);
        sysalert.runAllRules();
        Thread.sleep(1000);
        assert hitFlag[0];


//        postAgentTest.initIndex();
//        postAgentTest.postData(1, "");
    }
}