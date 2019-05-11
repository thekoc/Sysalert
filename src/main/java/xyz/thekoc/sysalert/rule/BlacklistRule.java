package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventTypes;

import java.util.List;
import java.util.Map;

public class BlacklistRule extends RuleType {

    public BlacklistRule(MonitoredEventTypes monitoredEventTypes) {
        super(monitoredEventTypes);
    }

    public BlacklistRule(String index, QueryBuilder... filters) {
        super(index, filters);
    }

    @Override
    public void addMatchedEvents(List<MatchedEvent> matchedEvents) {
        System.out.println("blacklist add:");
        for (MatchedEvent event: matchedEvents) {
            System.out.println(String.format("event_id: %d", (Integer) event.getSearchHit().getSourceAsMap().get("event_id")));
            if (monitoredEventTypes.contains((event.getMonitoredEventType()))) {
                ruleHits.addHit("Alerting!", "blacklist");
            }
        }
    }
}
