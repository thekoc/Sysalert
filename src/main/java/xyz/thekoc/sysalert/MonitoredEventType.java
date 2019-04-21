package xyz.thekoc.sysalert;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MonitoredEventType {
    private QueryBuilder filter;
    private int threshold;
    private String index = null;
    private String timestampField = "@timestamp";
    private Period queryDelay = Period.seconds(0);
    private Interval lastQueryInterval = null;
    private String timePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(timePattern);

    public MonitoredEventType(String index, QueryBuilder filter) {
        this(index, filter, 0);
    }

    public MonitoredEventType(String index, QueryBuilder filter, int threshold) {
        this.index = index;
        this.filter = filter;
        this.threshold = threshold;
    }

    public boolean isSameType(MonitoredEventType type) {
        return this.filter.equals(type.filter);
    }

    public QueryBuilder getFilter() {
        return filter;
    }

    public void setFilter(QueryBuilder filter) {
        this.filter = filter;
    }

    public int getThreshold() {
        return threshold;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Period getQueryDelay() {
        return queryDelay;
    }

    public void setQueryDelay(Period queryDelay) {
        this.queryDelay = queryDelay;
    }

    public Interval getLastQueryInterval() {
        return lastQueryInterval;
    }

    public void setLastQueryInterval(Interval lastQueryInterval) {
        this.lastQueryInterval = lastQueryInterval;
    }

    public String getTimestampField() {
        return timestampField;
    }

    public void setTimestampField(String timestampField) {
        this.timestampField = timestampField;
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

    public DateTime getDate(SearchHit hit) {
        String timestamp = (String) hit.getSourceAsMap().get(timestampField);
        return dateTimeFormatter.parseDateTime(timestamp);
    }
}
