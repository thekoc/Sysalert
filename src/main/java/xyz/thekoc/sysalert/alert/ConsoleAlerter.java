package xyz.thekoc.sysalert.alert;

import xyz.thekoc.sysalert.rule.RuleHit;

public class ConsoleAlerter extends Alerter {
    @Override
    public void alert(RuleHit ruleHit) {
        System.out.println(ruleHit.ruleType + ": " + ruleHit.message);
    }
}
