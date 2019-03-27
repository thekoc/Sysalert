package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.Duration;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventType;
import xyz.thekoc.sysalert.MonitoredEventTypes;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SquenceRule extends RuleType {
    private Queue<MatchedEvent> matchedEvents = new LinkedList<>();
    private Period timeWindow;

    SquenceRule(String index, Period timeWindow, QueryBuilder... filters) {
        super(index, new MonitoredEventTypes(filters));
        this.timeWindow = timeWindow;
    }

    @Override
    public void addMatchedEvents(List<MatchedEvent> matchedEvents) {
        for (MatchedEvent event: matchedEvents) {
            this.matchedEvents.add(event);
            while (shouldRemoveHead(event)) {
                this.matchedEvents.poll();
            }
            if (exists(matchedEvents, monitoredEventTypes)) {
                System.out.println("Alerting!");
            }
        }
    }

    private boolean shouldRemoveHead(MatchedEvent event) {
        if (matchedEvents.peek() != null) {
            boolean sequenceNotMatched = !matchedEvents.peek().isEventType(monitoredEventTypes.get(0));
            if (sequenceNotMatched) {
                return true;
            }
            boolean outOfWindow = new Duration(matchedEvents.peek().getDate(), event.getDate()).isLongerThan(
                    timeWindow.toStandardDuration());
            return outOfWindow;
        } else {
            return false;
        }
    }

    public boolean exists(List<MatchedEvent> matchedEvents, MonitoredEventTypes monitoredEventTypes) {
        int cursor = 0;
        for (MatchedEvent matchedEvent: matchedEvents) {
            if (cursor < monitoredEventTypes.size() && matchedEvent.isEventType(monitoredEventTypes.get(cursor))) {
                cursor += 1;
            }
        }
        return cursor == monitoredEventTypes.size();
    }
}
