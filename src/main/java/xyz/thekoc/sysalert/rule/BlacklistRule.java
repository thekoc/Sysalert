package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventTypes;

import java.util.List;

public class BlacklistRule extends RuleType {

    public BlacklistRule(String index, QueryBuilder... filters) {
        super(index, new MonitoredEventTypes(filters));
    }

    @Override
    public void addMatchedEvents(List<MatchedEvent> matchedEvents) {
        for (MatchedEvent event: matchedEvents) {
            if (monitoredEventTypes.contains((event.getMonitoredEventType()))) {
                ruleHits.addHit("Alerting!", "blacklist");
            }
        }
    }
}
