package xyz.thekoc.sysalert;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.joda.time.DateTime;


public class MatchedEvent {
    private SearchHit searchHit;
    private DateTime date;
    private MonitoredEventType monitoredEventType;
    public MatchedEvent (SearchHit searchHit, MonitoredEventType monitoredEventType, DateTime date) {
        this.searchHit = searchHit;
        this.monitoredEventType = monitoredEventType;
        this.date = date;
    }

    public SearchHit getSearchHit() {
        return searchHit;
    }

    public DateTime getDate() {
        return date;
    }

    public QueryBuilder getFilter() {
        return monitoredEventType.getFilter();
    }

    public boolean isEventType(MonitoredEventType monitoredEventType) {
        return monitoredEventType.isSameType(this.monitoredEventType);
    }

    public MonitoredEventType getMonitoredEventType() {
        return monitoredEventType;
    }
}
