package xyz.thekoc.sysalert;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.DateTime;
import org.joda.time.Period;
import xyz.thekoc.sysalert.rule.CombinationRule;
import xyz.thekoc.sysalert.rule.FrequencyRule;
import xyz.thekoc.sysalert.rule.SysmonFrequencyRule;


public class SysalertTest {

    @org.junit.Test
    public void start() {
        System.out.println(DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
    }

    @org.junit.Test
    public void main() {
        Sysalert sysalert = new Sysalert("localhost", 9200, "http");
        QueryBuilder event3 = QueryBuilders.termQuery("event_id", 3);
        QueryBuilder event4 = QueryBuilders.termQuery("event_id", 4);
        FrequencyRule frequencyRule = new SysmonFrequencyRule("sysalert-test", Period.minutes(1), event3, 200);
        frequencyRule.setQueryDelay(Period.seconds(1));
        sysalert.addRule(frequencyRule);

        CombinationRule combinationRule = new CombinationRule("sysalert-test", Period.minutes(1),
                new MonitoredEventType(event3, 1), new MonitoredEventType(event4, 1));
//        sysalert.addRule(combinationRule);
        new Thread(() -> new PostAgentTest().postData()).start();

        sysalert.start();
    }
}