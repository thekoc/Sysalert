package xyz.thekoc.sysalert.rule;

import java.util.ArrayList;
import java.util.Iterator;

public class RuleHits implements Iterable<RuleHit> {
    private ArrayList<RuleHit> ruleHits = new ArrayList<>();

    public void addHit(String message, String type) {
        ruleHits.add(new RuleHit(message, type));
    }

    @Override
    public Iterator<RuleHit> iterator() {
        return ruleHits.iterator();
    }
}
