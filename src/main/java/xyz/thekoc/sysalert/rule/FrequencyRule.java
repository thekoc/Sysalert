package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MatchedEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class FrequencyRule extends RuleType {
    private LinkedList<MatchedEvent> matchedEvents = new LinkedList<>();
    private Period timeWindow = Period.minutes(1);
    private int threshold;
    public FrequencyRule(String index, QueryBuilder filter, int threshold, Period timeWindow) {
        super(index, filter, threshold);
        this.threshold = threshold;
        this.timeWindow = timeWindow;
    }


    @Override
    public void addMatchedEvents(ArrayList<MatchedEvent> matchedEvents) {
        System.out.println("hit num: ".concat(String.valueOf(matchedEvents.size())));
        for (MatchedEvent event: matchedEvents) {
            this.matchedEvents.add(event);
            while (this.matchedEvents.size() > 0 &&
                    new Duration(this.matchedEvents.peek().getDate(), event.getDate()).isLongerThan(
                            timeWindow.toStandardDuration())) {
                this.matchedEvents.poll();
                System.out.println("polling");

            }

            if (this.matchedEvents.size() > threshold) {
//                System.out.println("Alerting!");
            }
        }
    }
}
