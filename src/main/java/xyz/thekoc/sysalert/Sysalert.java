package xyz.thekoc.sysalert;

import com.esotericsoftware.yamlbeans.YamlException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import xyz.thekoc.sysalert.conifg.Config;
import xyz.thekoc.sysalert.rule.RuleType;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Sysalert {
    private boolean running = true;
    private ArrayList<RuleType> rules = new ArrayList<RuleType>();
    private SearchAgent searchAgent;
    private DateTime startDateTime = null;
    public Sysalert(String hostname, int port, String scheme) {
        searchAgent = new SearchAgent(hostname, port, scheme);
    }

    public void addRule(RuleType rule) {
        rules.add(rule);
    }

    public void start() {
        startDateTime = DateTime.now();
        while (running) {
            runAllRules();
            sleep();
        }
    }

    private void sleep() {
        System.out.println("sleeping");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runAllRules() {
        for (RuleType rule: rules) {
            Interval queryInterval;
            if (rule.getLastQueryInterval() == null) {
                queryInterval = new Interval(startDateTime.minus(rule.getQueryDelay()), DateTime.now().minus(rule.getQueryDelay()));
            } else {
                queryInterval = new Interval(rule.getLastQueryInterval().getEnd(), DateTime.now().minus(rule.getQueryDelay()));
            }


            for (MonitoredEventType eventType: rule.getMonitoredEventTypes()) {
                runQuery(rule, eventType, queryInterval);
            }
            rule.setLastQueryInterval(queryInterval);
        }
    }

    private void runQuery(RuleType rule, MonitoredEventType eventType, Interval queryInterval) {
        String startTime = queryInterval.getStart().toString(rule.getDateTimeFormatter());
        String endTime = queryInterval.getEnd().toString(rule.getDateTimeFormatter());

        QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp").
                gte(startTime).lt(endTime).format(rule.getTimePattern());
        QueryBuilder finalQuery = QueryBuilders.boolQuery().filter(eventType.getFilter()).filter(rangeQueryBuilder);
        FieldSortBuilder sort = new FieldSortBuilder(rule.getTimestampField()).order(SortOrder.ASC);
        SearchResponse response = searchAgent.search(rule.getIndex(), finalQuery, sort);
//        System.out.println("real total heat: " + response.getHits().totalHits);
//        System.out.println(rangeQueryBuilder);
        ArrayList<MatchedEvent> matchedEvents = new ArrayList<>();
        for (SearchHit hit: response.getHits()) {
            matchedEvents.add(new MatchedEvent(hit, eventType, rule.getDate(hit)));
        }
        rule.addMatchedEvents(matchedEvents);
    }

    public static void main(String[] args) {
        // TODO: parse command line arguments
        // TODO: add rules from `rule_folder`
        Config config = Config.getConfig();
        String rulePath = Sysalert.class.getClassLoader().getResource("test_rule.yml").getPath();
        try {
            config.addRule(rulePath);
        } catch (FileNotFoundException | YamlException e) {
            e.printStackTrace();
        }
        Sysalert s = new Sysalert(config.getHostname(), config.getPort(), config.getScheme());
        s.start();
    }
}
