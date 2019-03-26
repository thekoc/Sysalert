package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import xyz.thekoc.sysalert.MatchedEvent;

import java.util.ArrayList;

public class BlacklistRule extends RuleType {

    public BlacklistRule(String index, QueryBuilder filter) {
        super(index, filter);
    }

    @Override
    public void addMatchedEvents(ArrayList<MatchedEvent> matchedEvents) {

    }
}
