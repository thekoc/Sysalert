package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.Duration;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MatchedEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FrequencyRule extends RuleType {
    private Queue<MatchedEvent> matchedEvents = new LinkedList<>();
    private Period timeWindow;
    private int threshold;
    public FrequencyRule(String index, Period timeWindow, QueryBuilder filter, int threshold)  {
        super(index, filter, threshold);
        this.threshold = threshold;
        this.timeWindow = timeWindow;
    }


    @Override
    public void addMatchedEvents(List<MatchedEvent> matchedEvents) {
        System.out.println("hit num: ".concat(String.valueOf(matchedEvents.size())));
        for (MatchedEvent event: matchedEvents) {
            this.matchedEvents.add(event);
            while (this.matchedEvents.size() > 0 &&
                    new Duration(this.matchedEvents.peek().getDate(), event.getDate()).isLongerThan(
                            timeWindow.toStandardDuration())) {
                this.matchedEvents.poll();
            }

            if (this.matchedEvents.size() > threshold) {
                ruleHits.addHit("Alerting!", "frequency");
            }
        }
    }
}
