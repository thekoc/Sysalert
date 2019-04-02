package xyz.thekoc.sysalert.rule;

import java.util.Iterator;
import java.util.LinkedList;

public class RuleHits implements Iterable<RuleHit> {
    private LinkedList<RuleHit> ruleHits = new LinkedList<>();

    public void addHit(String message, String type) {
        ruleHits.add(new RuleHit(message, type));
    }

    @Override
    public Iterator<RuleHit> iterator() {
        return ruleHits.iterator();
    }

    public void add(RuleHit hit) {
        ruleHits.add(hit);
    }

    public RuleHit poll() {
        return ruleHits.poll();
    }

}
