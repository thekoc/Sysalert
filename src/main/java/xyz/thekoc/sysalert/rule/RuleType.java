package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventType;
import xyz.thekoc.sysalert.MonitoredEventTypes;
import xyz.thekoc.sysalert.alert.Alerter;

import java.util.ArrayList;
import java.util.List;

public abstract class RuleType {
    protected String index;
    MonitoredEventTypes monitoredEventTypes;
    private String timestampField = "@timestamp";
    private Period queryDelay = Period.seconds(0);
    protected RuleHits ruleHits = new RuleHits();
    private List<Alerter> alerts = new ArrayList<>();
    private Interval lastQueryInterval = null;
    private String timePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(timePattern);

    RuleType(String index, MonitoredEventTypes monitoredEventTypes) {
        this.index = index;
        this.monitoredEventTypes = monitoredEventTypes;
    }

    RuleType(String index, QueryBuilder filter) {
        this(index, filter, 0);
    }

    RuleType(String index, QueryBuilder filter, int threshold) {
        this(index, new MonitoredEventTypes());
        monitoredEventTypes.add(new MonitoredEventType(filter, threshold));
    }

    public abstract void addMatchedEvents(List<MatchedEvent> matchedEvents);


    public void addFilterToAll(QueryBuilder filter) {
        monitoredEventTypes.addFilterToAll(filter);
    }

    public String getIndex() {
        return index;
    }

    public DateTime getDate(SearchHit hit) {
        String timestamp = (String) hit.getSourceAsMap().get(timestampField);
        return dateTimeFormatter.parseDateTime(timestamp);
    }

    public String getTimestampField() {
        return timestampField;
    }

    public Interval getLastQueryInterval() {
        return lastQueryInterval;
    }

    public void setLastQueryInterval(Interval lastQueryInterval) {
        this.lastQueryInterval = lastQueryInterval;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public String getTimePattern() {
        return timePattern;
    }

    public void setTimePattern(String timePattern) {
        this.timePattern = timePattern;
        dateTimeFormatter = DateTimeFormat.forPattern(timePattern);
    }

    public MonitoredEventTypes getMonitoredEventTypes() {
        return monitoredEventTypes;
    }

    public Period getQueryDelay() {
        return queryDelay;
    }

    public void setQueryDelay(Period queryDelay) {
        this.queryDelay = queryDelay;
    }

    public RuleHits getRuleHits() {
        return ruleHits;
    }

    public List<Alerter> getAlerters() {
        return alerts;
    }

    public void addAlerter(Alerter alerter) {
        alerts.add(alerter);
    }
}

