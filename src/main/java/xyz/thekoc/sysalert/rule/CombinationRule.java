package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.Duration;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventType;
import xyz.thekoc.sysalert.MonitoredEventTypes;

import java.util.*;

public class CombinationRule extends RuleType {
    private Queue<MatchedEvent> matchedEvents = new LinkedList<>();
    private Period timeWindow;
    private Map<MonitoredEventType, Integer> windowEventCounter = new Hashtable<>();

    public CombinationRule(String index, Period timeWindow, MonitoredEventType... monitoredEventTypes) {
        super(index, new MonitoredEventTypes(Arrays.asList(monitoredEventTypes)));
        this.timeWindow = timeWindow;
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
            windowEventCounter.put(event.getMonitoredEventType(), previousCount + 1);


            while (this.matchedEvents.size() > 0 &&
                    new Duration(this.matchedEvents.peek().getDate(), event.getDate()).isLongerThan(
                            timeWindow.toStandardDuration())) {

                MatchedEvent head = this.matchedEvents.poll();
                if (head != null) {
                    Integer headCount = windowEventCounter.get(head.getMonitoredEventType());
                }
                windowEventCounter.put(event.getMonitoredEventType(), previousCount - 1);
            }

            boolean alertFlag = true;
            for (MonitoredEventType type: monitoredEventTypes) {
                if (windowEventCounter.get(type) < type.getThreshold()) {
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
