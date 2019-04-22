package xyz.thekoc.sysalert;

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

    public void extend(RuleHits hits) {
        for (RuleHit hit: hits) {
            add(hit);
        }
    }

    public void clear() {
        ruleHits.clear();
    }

    public RuleHit poll() {
        return ruleHits.poll();
    }

    public int size() {
        return ruleHits.size();
    }

}
