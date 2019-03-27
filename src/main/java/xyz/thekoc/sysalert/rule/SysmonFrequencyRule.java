package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.Period;


public class SysmonFrequencyRule extends FrequencyRule {
    public SysmonFrequencyRule(String index, Period timeWindow, QueryBuilder filter, int threshold) {
        super(index, timeWindow, filter, threshold);
        SysmonRuleBuilder.addSysmonFilter(this);
    }
}
