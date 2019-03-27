package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.Duration;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventType;
import xyz.thekoc.sysalert.MonitoredEventTypes;

import java.util.*;
import java.util.stream.Collectors;

public class CombinationRule extends RuleType {
    private Queue<MatchedEvent> matchedEvents = new LinkedList<>();
    private Period timeWindow;
    private Map<MonitoredEventType, Integer> windowEventCounter = new Hashtable<>();

    public CombinationRule(String index, Period timeWindow, QueryBuilder... filters) {
        this(index, timeWindow, new MonitoredEventTypes(Arrays.stream(filters).map((filter) -> new MonitoredEventType(filter, 1)).collect(Collectors.toList())));
    }
    public CombinationRule(String index, Period timeWindow, MonitoredEventType... monitoredEventTypes) {
        this(index, timeWindow, new MonitoredEventTypes(Arrays.asList(monitoredEventTypes)));
    }
    public CombinationRule(String index, Period timeWindow, MonitoredEventTypes monitoredEventTypes) {
        super(index, monitoredEventTypes);
        this.timeWindow = timeWindow;
    }

    @Override
    public void addMatchedEvents(ArrayList<MatchedEvent> matchedEvents) {
        for (MatchedEvent event: matchedEvents) {
            this.matchedEvents.add(event);
            Integer previousCount = windowEventCounter.get(event.getMonitoredEventType());
            previousCount = previousCount == null ? 0 : previousCount;
            windowEventCounter.put(event.getMonitoredEventType(), previousCount + 1);


            while (this.matchedEvents.size() > 0 &&
                    new Duration(this.matchedEvents.peek().getDate(), event.getDate()).isLongerThan(
                            timeWindow.toStandardDuration())) {

                MatchedEvent head = this.matchedEvents.poll();
                if (head != null) {
                    Integer headCount = windowEventCounter.get(head.getMonitoredEventType());
                    headCount = headCount == null ? 0 : headCount;
                    windowEventCounter.put(head.getMonitoredEventType(), headCount - 1);
                }
            }

            boolean alertFlag = true;
            for (MonitoredEventType type: monitoredEventTypes) {
                Integer currentCount = windowEventCounter.get(type);
                currentCount = currentCount == null ? 0 : currentCount;
                if (currentCount < type.getThreshold()) {
                    alertFlag = false;
                    break;
                }
            }

            if (alertFlag) {
                System.out.println("Combanation Alerting!!!!!");
            }
        }
    }
}
