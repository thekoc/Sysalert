package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.RuleHit;

import java.util.List;

public class ChangeRule extends RuleType {
    private boolean firstEvent = true;
    private String field;
    private Object initialValue = null;

    ChangeRule(String index, QueryBuilder filter, String field) {
        super(index, filter);
        this.field = field;
    }

    @Override
    public void addMatchedEvents(List<MatchedEvent> matchedEvents) {
        for (MatchedEvent event: matchedEvents) {
            Object value = event.getSearchHit().getSourceAsMap().get(field);
            if (firstEvent) {
                firstEvent = false;
                initialValue = value;
            } else {
                if (initialValue.equals(value)) {
                    ruleHits.addHit("CHANGE RULE！", "change");
                }
            }
        }
    }
}
