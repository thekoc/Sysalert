package xyz.thekoc.sysalert.conifg;

import com.google.gson.Gson;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MonitoredEventType;
import xyz.thekoc.sysalert.MonitoredEventTypes;
import xyz.thekoc.sysalert.rule.*;

import java.util.*;

public class RuleBuilder {
    static RuleType fromRuleBean(RuleBean ruleBean) {
        Config config = Config.getConfig();
        String index = ruleBean.index != null ? ruleBean.index : config.getIndex();

        Period timeWindow = Period.ZERO;
        TimeWindowBean timeWindowBean = ruleBean.timewindow;
        timeWindow.plus(Period.days(timeWindowBean.days));
        timeWindow.plus(Period.hours(timeWindowBean.hours));
        timeWindow.plus(Period.minutes(timeWindowBean.minutes));
        timeWindow.plus(Period.seconds(timeWindowBean.seconds));

        switch (ruleBean.type) {
            case "frequency_rule":
                return new FrequencyRule(index, timeWindow, getFilter(ruleBean.filter), ruleBean.num_events);
            case "sysmon_frequency_rule":
                return new SysmonFrequencyRule(index, timeWindow, getFilter(ruleBean.filter), ruleBean.num_events);
            case "combination_rule":
                MonitoredEventTypes combinationRuleTypes = new MonitoredEventTypes();
                for (Map filter: ruleBean.filter) {
                    combinationRuleTypes.add(new MonitoredEventType(getFilter(Collections.singletonList(filter))));
                }
                return new CombinationRule(index, timeWindow, combinationRuleTypes);
            case "sequence_rule":
                MonitoredEventTypes sequenceRuleTypes = new MonitoredEventTypes();
                for (Map filter: ruleBean.filter) {
                    sequenceRuleTypes.add(new MonitoredEventType(getFilter(Collections.singletonList(filter))));
                }
                return new SequenceRule(index, timeWindow, sequenceRuleTypes);
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
