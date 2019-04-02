package xyz.thekoc.sysalert.rule;

public class RuleHit {
    public String message;
    public String ruleType;
    RuleHit(String message, String ruleType) {
        this.message = message;
        this.ruleType = ruleType;
    }
}
