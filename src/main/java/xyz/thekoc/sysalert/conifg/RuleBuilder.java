package xyz.thekoc.sysalert.conifg;

import com.google.gson.Gson;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MonitoredEventType;
import xyz.thekoc.sysalert.MonitoredEventTypes;
import xyz.thekoc.sysalert.rule.CombinationRule;
import xyz.thekoc.sysalert.rule.FrequencyRule;
import xyz.thekoc.sysalert.rule.RuleType;
import xyz.thekoc.sysalert.rule.SysmonFrequencyRule;

import java.util.*;

public class RuleBuilder {
    static RuleType fromRuleBean(RuleBean ruleBean) {
        Config config = Config.getConfig();
        String index = ruleBean.index != null ? ruleBean.index : config.getIndex();

        Period timeWindow = Period.ZERO;
        Map<String, Integer> timeWindowBean = ruleBean.timewindow;
        if (timeWindowBean.get("days") != null) {
            timeWindow.plus(Period.days(timeWindowBean.get("days")));
        }
        if (timeWindowBean.get("hours") != null) {
            timeWindow.plus(Period.hours(timeWindowBean.get("hours")));
        }
        if (timeWindowBean.get("minutes") != null) {
            timeWindow.plus(Period.minutes(timeWindowBean.get("minutes")));
        }
        if (timeWindowBean.get("seconds") != null) {
            timeWindow.plus(Period.seconds(timeWindowBean.get("seconds")));
        }




        switch (ruleBean.type) {
            case "frequency_rule":
                return new FrequencyRule(index, timeWindow, getFilter(ruleBean.filter), ruleBean.num_events);
            case "sysmon_frequency_rule":
                return new SysmonFrequencyRule(index, timeWindow, getFilter(ruleBean.filter), ruleBean.num_events);
            case "combination_rule":
                MonitoredEventTypes monitoredEventTypes = new MonitoredEventTypes();
                for (Map filter: ruleBean.filter) {
                    monitoredEventTypes.add(new MonitoredEventType(getFilter(Collections.singletonList(filter))));
                }
                return new CombinationRule(index, timeWindow, monitoredEventTypes);
            default:
                return null;
        }
    }

    private static QueryBuilder getFilter(List filter) {
        Gson gson = new Gson();
        HashMap<String, Object> filters = new HashMap<String, Object>();
        filters.put("filter", filter);
        HashMap<String, Map> boolQuery = new HashMap<>();
        boolQuery.put("bool", filters);
        String string = gson.toJson(boolQuery);
        return QueryBuilders.wrapperQuery(string);
    }
}
