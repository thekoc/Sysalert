package xyz.thekoc.sysalert;

import org.elasticsearch.index.query.QueryBuilder;

public class MonitoredEventType {
    private QueryBuilder filter;
    private int threshold;
    public MonitoredEventType(QueryBuilder filter, int threshold) {
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
}
