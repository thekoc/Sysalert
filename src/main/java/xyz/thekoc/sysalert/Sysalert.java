package xyz.thekoc.sysalert;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import xyz.thekoc.sysalert.rule.RuleType;

import java.util.ArrayList;
import java.util.Arrays;

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
                queryInterval = new Interval(startDateTime, DateTime.now());
            } else {
                queryInterval = new Interval(rule.getLastQueryInterval().getEnd(), DateTime.now());
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
        ArrayList<MatchedEvent> matchedEvents = new ArrayList<>();
        for (SearchHit hit: response.getHits()) {
            matchedEvents.add(new MatchedEvent(hit, eventType, rule.getDate(hit)));
        }
        rule.addMatchedEvents(matchedEvents);
    }

    public static void main(String[] args) {
        Sysalert s = new Sysalert("localhost", 9200, "http");
        s.start();
    }
}
