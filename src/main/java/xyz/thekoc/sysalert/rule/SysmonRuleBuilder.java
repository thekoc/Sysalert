package xyz.thekoc.sysalert.rule;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class SysmonRuleBuilder {
    private static QueryBuilder sysmonFilter = QueryBuilders.matchPhraseQuery("source_name", "Microsoft-Windows-Sysmon");


    public static void addSysmonFilter(RuleType rule) {
        rule.addFilterToAll(sysmonFilter);
    }
}
