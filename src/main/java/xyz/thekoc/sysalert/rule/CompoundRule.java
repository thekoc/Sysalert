package xyz.thekoc.sysalert.rule;

import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.MonitoredEventTypes;

import java.util.Collections;
import java.util.List;


public class CompoundRule extends RuleType {
    public enum Operator {
        AND,
        OR
    }

    private Operator operator;
    private RuleType[] rules;
    public CompoundRule(Operator operator, RuleType ...rules) {
        super(new MonitoredEventTypes());
        this.rules = rules;
        this.operator = operator;
    }

    @Override
    public MonitoredEventTypes getMonitoredEventTypes() {
        MonitoredEventTypes compoundEventTypes = new MonitoredEventTypes();
        for (RuleType rule: rules) {
            compoundEventTypes.extend(rule.getMonitoredEventTypes());
        }
        return compoundEventTypes;
    }

    @Override
    public void addMatchedEvents(List<MatchedEvent> matchedEvents) {
        for (MatchedEvent matchedEvent: matchedEvents) {
            for (RuleType rule: rules) {
                if (rule.getMonitoredEventTypes().contains(matchedEvent.getMonitoredEventType()))
                rule.addMatchedEvents(Collections.singletonList(matchedEvent));
            }
        }

        boolean hitFlag = false;
        switch (operator) {
            case OR:
                for (RuleType rule: rules) {
                    if (rule.getRuleHits().size() > 0) {
                        hitFlag = true;
                    }
                }
                break;
            case AND:
                hitFlag = true;
                for (RuleType rule: rules) {
                    if (rule.getRuleHits().size() == 0) {
                        hitFlag = false;
                        break;
                    }
                }
                break;
        }
        if (hitFlag) {
            for (RuleType rule: rules) {
                getRuleHits().extend(rule.getRuleHits());
                rule.getRuleHits().clear();
            }
        }
    }
}
