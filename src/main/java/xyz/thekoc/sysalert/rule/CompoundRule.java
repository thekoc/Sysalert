package xyz.thekoc.sysalert.rule;

import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventTypes;

import java.util.List;


enum Oprator {
    AND,
    OR
}

public class CompoundRule extends RuleType {
    private Oprator oprator = null;
    CompoundRule(Oprator oprator, MonitoredEventTypes monitoredEventTypes) {
        super(monitoredEventTypes);
        this.oprator = oprator;
    }

    @Override
    public void addMatchedEvents(List<MatchedEvent> matchedEvents) {

    }
}
