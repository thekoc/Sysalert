package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventType;
import xyz.thekoc.sysalert.MonitoredEventTypes;
import xyz.thekoc.sysalert.RuleHits;
import xyz.thekoc.sysalert.alert.Alerter;

import java.util.ArrayList;
import java.util.List;

public abstract class RuleType {
    MonitoredEventTypes monitoredEventTypes;
    protected RuleHits ruleHits = new RuleHits();
    private List<Alerter> alerts = new ArrayList<>();

    RuleType(MonitoredEventTypes monitoredEventTypes) {
        this.monitoredEventTypes = monitoredEventTypes;
    }

    RuleType(String index, QueryBuilder filter) {
        this(index, filter, 0);
    }

    RuleType(String index, QueryBuilder ...filters) {
        this(new MonitoredEventTypes(index, filters));
    }

    RuleType(String index, QueryBuilder filter, int threshold) {
        this(new MonitoredEventTypes());
        monitoredEventTypes.add(new MonitoredEventType(index, filter, threshold));
    }

    public abstract void addMatchedEvents(List<MatchedEvent> matchedEvents);


    public void addFilterToAll(QueryBuilder filter) {
        monitoredEventTypes.addFilterToAll(filter);
    }


    public MonitoredEventTypes getMonitoredEventTypes() {
        return monitoredEventTypes;
    }

    public void setQueryDelayForAll(Period queryDelay) {
        for (MonitoredEventType monitoredEventType: getMonitoredEventTypes()) {
            monitoredEventType.setQueryDelay(queryDelay);
        }
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

