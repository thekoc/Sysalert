package xyz.thekoc.sysalert.util;

import org.elasticsearch.index.query.QueryBuilder;

public class EventNumber {
    public QueryBuilder filter;
    public int number;
    EventNumber(QueryBuilder filter, int number) {
        this.filter = filter;
        this.number = number;
    }
}
