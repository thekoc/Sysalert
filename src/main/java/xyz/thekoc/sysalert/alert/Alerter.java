package xyz.thekoc.sysalert.alert;

import xyz.thekoc.sysalert.RuleHit;

public abstract class Alerter {
    abstract public void alert(RuleHit ruleHit);
}
