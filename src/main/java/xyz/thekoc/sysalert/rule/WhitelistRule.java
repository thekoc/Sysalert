package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventTypes;

import java.util.List;

public class WhitelistRule extends RuleType {

    public WhitelistRule(String index, QueryBuilder... filters) {
        super(index, filters);
    }

    @Override
    public void addMatchedEvents(List<MatchedEvent> matchedEvents) {
        for (MatchedEvent event: matchedEvents) {
            if (!monitoredEventTypes.contains((event.getMonitoredEventType()))) {
                ruleHits.addHit("Alerting!", "whitelist");
            }
        }
    }
}

