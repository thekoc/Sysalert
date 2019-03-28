package xyz.thekoc.sysalert;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.Period;
import xyz.thekoc.sysalert.conifg.Config;
import xyz.thekoc.sysalert.rule.CombinationRule;
import xyz.thekoc.sysalert.rule.FrequencyRule;
import xyz.thekoc.sysalert.rule.SequenceRule;
import xyz.thekoc.sysalert.rule.SysmonFrequencyRule;

import java.io.FileNotFoundException;


public class SysalertTest {

    @org.junit.Test
    public void start() {

    }

    @org.junit.Test
    public void main() {


        Sysalert sysalert = new Sysalert("localhost", 9200, "http");
        QueryBuilder event3 = QueryBuilders.termQuery("event_id", 3);
        QueryBuilder event4 = QueryBuilders.termQuery("event_id", 4);
        QueryBuilder event5 = QueryBuilders.termQuery("event_id", 5);
        FrequencyRule frequencyRule = new SysmonFrequencyRule("sysalert-test", Period.minutes(1), event3, 200);
        frequencyRule.setQueryDelay(Period.seconds(1));
//        sysalert.addRule(frequencyRule);

        CombinationRule combinationRule = new CombinationRule("sysalert-test", Period.minutes(1),
                new MonitoredEventType(event3, 1), new MonitoredEventType(event4, 1));
//        sysalert.addRule(combinationRule);


        SequenceRule sequenceRule = new SequenceRule("sysalert-test", Period.minutes(15), event3, event4, event5);
        sysalert.addRule(sequenceRule);
//        new Thread(() -> new PostAgentTest().postData()).start();

        sysalert.start();
    }
}