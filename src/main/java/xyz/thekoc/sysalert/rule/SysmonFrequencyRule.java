package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.Period;


public class SysmonFrequencyRule extends FrequencyRule {
    public SysmonFrequencyRule(String index, QueryBuilder filter, int threshold, Period timewindow) {
        super(index, timewindow, filter, threshold);
        SysmonRuleBuilder.addSysmonFilter(this);
    }
}
