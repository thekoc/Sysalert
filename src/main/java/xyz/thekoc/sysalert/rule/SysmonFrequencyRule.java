package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.Period;


public class SysmonFrequencyRule extends FrequencyRule {
    public SysmonFrequencyRule(String index, Period timewindow, QueryBuilder filter, int threshold) {
        super(index, timewindow, filter, threshold);
        SysmonRuleBuilder.addSysmonFilter(this);
    }
}
