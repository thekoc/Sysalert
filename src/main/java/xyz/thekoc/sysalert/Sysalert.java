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
import org.joda.time.Period;
import xyz.thekoc.sysalert.agent.SearchAgent;
import xyz.thekoc.sysalert.alert.Alerter;
import xyz.thekoc.sysalert.conifg.Config;
import xyz.thekoc.sysalert.conifg.FieldMissingException;
import xyz.thekoc.sysalert.conifg.FieldValueException;
import xyz.thekoc.sysalert.conifg.NoSuchRuleException;
import xyz.thekoc.sysalert.rule.RuleType;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;

public class Sysalert {
    private boolean running = true;
    private ArrayList<RuleType> rules = new ArrayList<>();
    private SearchAgent searchAgent;
    private DateTime startDateTime = null;
    private Period runEvery = Period.seconds(10);
    public Sysalert(String hostname, int port, String scheme) {
        searchAgent = new SearchAgent(hostname, port, scheme);
    }

    public Sysalert(Config config) {
        searchAgent = new SearchAgent(config.getHostname(), config.getPort(), config.getScheme());
        runEvery = config.getRunEvery();
        addRules(config.getRuleTypes());
    }

    public void addRule(RuleType rule) {
        rules.add(rule);
    }

    public void addRules(Iterable<RuleType> rules) {
        for (RuleType rule: rules) {
            addRule(rule);
        }
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
            DateTime start = new DateTime();
            DateTime end = start.plus(runEvery);
            long millis = end.getMillis() - start.getMillis();
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Interval updateQueryInterval(MonitoredEventType eventType) {
        Interval queryInterval;
        if (eventType.getLastQueryInterval() == null) {
            queryInterval = new Interval(startDateTime.minus(eventType.getQueryDelay()), DateTime.now().minus(eventType.getQueryDelay()));
        } else {
            queryInterval = new Interval(eventType.getLastQueryInterval().getEnd(), DateTime.now().minus(eventType.getQueryDelay()));
        }
        eventType.setLastQueryInterval(queryInterval);
        return queryInterval;
    }

    public void runAllRules() {
        for (RuleType rule: rules) {
            ArrayList<MatchedEvent> matchedEvents = new ArrayList<>();
            for (MonitoredEventType eventType: rule.getMonitoredEventTypes()) {
                Interval newInterval = updateQueryInterval(eventType);
                matchedEvents.addAll(runQuery(eventType, newInterval));

            }

            matchedEvents.sort(Comparator.comparing(MatchedEvent::getDate));
            rule.addMatchedEvents(matchedEvents);
            RuleHit ruleHit = rule.getRuleHits().poll();
            while (ruleHit != null) {
                for (Alerter alerter: rule.getAlerters()) {
                    alerter.alert(ruleHit);
                }
                ruleHit = rule.getRuleHits().poll();
            }
        }
    }

    private ArrayList<MatchedEvent> runQuery(MonitoredEventType eventType, Interval queryInterval) {
        // TODO: Support scroll
        String startTime = queryInterval.getStart().toString(eventType.getDateTimeFormatter());
        String endTime = queryInterval.getEnd().toString(eventType.getDateTimeFormatter());

        QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp").
                gte(startTime).lt(endTime).format(eventType.getTimePattern());
        QueryBuilder finalQuery = QueryBuilders.boolQuery().filter(eventType.getFilter()).filter(rangeQueryBuilder);
        FieldSortBuilder sort = new FieldSortBuilder(eventType.getTimestampField()).order(SortOrder.ASC);
        SearchResponse response = searchAgent.search(eventType.getIndex(), finalQuery, sort);
        ArrayList<MatchedEvent> matchedEvents = new ArrayList<>();
        for (SearchHit hit: response.getHits()) {
            matchedEvents.add(new MatchedEvent(hit, eventType, eventType.getDate(hit)));
        }
        return matchedEvents;
    }

    public void init() {
        startDateTime = DateTime.now();
    }


    public static void main(String[] args) throws FileNotFoundException, FieldMissingException, YamlException, NoSuchRuleException, FieldValueException {
        Config.init(args);
        Config config = Config.getConfig();

        Sysalert s = new Sysalert(config);
        s.start();
    }
}
