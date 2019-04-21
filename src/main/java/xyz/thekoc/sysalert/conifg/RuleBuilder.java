package xyz.thekoc.sysalert.conifg;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.gson.Gson;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MonitoredEventType;
import xyz.thekoc.sysalert.MonitoredEventTypes;
import xyz.thekoc.sysalert.alert.ConsoleAlerter;
import xyz.thekoc.sysalert.alert.PopupAlerter;
import xyz.thekoc.sysalert.rule.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

class RuleBuilder {
    static RuleType fromFile(String pathname) throws FileNotFoundException, YamlException, FieldMissingException, NoSuchRuleException {
        YamlReader rawReader = new YamlReader(new FileReader(pathname));
        Map rawYamlObject = (Map) rawReader.read();

        Config config = Config.getConfig();
        String index = rawYamlObject.get("index") != null ? (String) rawYamlObject.get("index") : config.getIndex();
        String type = (String) rawYamlObject.get("type");
        if (type == null) {
            throw new YamlException("type field cannot be null!");
        } else if (Arrays.asList("frequency", "sysmon_frequency", "combination", "sequence", "blacklist").contains(type)) {
            YamlReader reader = new YamlReader(new FileReader(pathname));
            reader.getConfig().setPropertyDefaultType(RuleBean.class, "timewindow", PeriodBean.class);
            RuleBean ruleBean = reader.read(RuleBean.class);


            // Verify the fields
            switch (ruleBean.type) {
                case "blacklist":
                    if (ruleBean.filter == null) {
                        throw new FieldMissingException();
                    }
                    break;
                case "frequency":
                case "sysmon_frequency":
                    if (
                            ruleBean.num_events == null ||
                            ruleBean.timewindow == null ||
                            ruleBean.filter == null
                    ) {
                        throw new FieldMissingException();
                    }
                    break;
                case "combination":
                    if (ruleBean.combination == null) {
                        throw new FieldMissingException();
                    }
                    break;
                case "sequence":
                    if (ruleBean.sequence == null) {
                        throw new FieldMissingException();
                    }
                    break;
            }

            Period timeWindow = periodFromPeriodBean(ruleBean.timewindow);
            Period queryDelay = periodFromPeriodBean(ruleBean.query_delay);
            RuleType newRule;

            switch (ruleBean.type) {
                case "blacklist":
                    newRule = new BlacklistRule(index, getFilter(ruleBean.filter));
                    break;
                case "frequency":
                    newRule = new FrequencyRule(index, timeWindow, getFilter(ruleBean.filter), ruleBean.num_events);
                    break;
                case "sysmon_frequency":
                    newRule = new SysmonFrequencyRule(index, timeWindow, getFilter(ruleBean.filter), ruleBean.num_events);
                    break;
                case "combination":
                    MonitoredEventTypes combinationRuleTypes = new MonitoredEventTypes();
                    for (Map filter: ruleBean.combination) {
                        combinationRuleTypes.add(new MonitoredEventType(getFilter(Collections.singletonList(filter))));
                    }
                    combinationRuleTypes.addFilterToAll(getFilter(ruleBean.filter));
                    newRule = new CombinationRule(index, timeWindow, combinationRuleTypes);
                    break;
                case "sequence":
                    MonitoredEventTypes sequenceRuleTypes = new MonitoredEventTypes();
                    for (Map filter: ruleBean.sequence) {
                        sequenceRuleTypes.add(new MonitoredEventType(getFilter(Collections.singletonList(filter))));
                    }
                    sequenceRuleTypes.addFilterToAll(getFilter(ruleBean.filter));
                    newRule = new CombinationRule(index, timeWindow, sequenceRuleTypes);
                    break;
                default:
                    throw new NoSuchRuleException("No such rule: " + ruleBean.type);
            }

            newRule.setQueryDelay(queryDelay);

            // TODO: config the alerters
            newRule.addAlerter(new ConsoleAlerter());
            newRule.addAlerter(new PopupAlerter());
            return newRule;
        }
        return null;
    }

    private static Period periodFromPeriodBean(PeriodBean periodBean) {
        Period period = Period.ZERO;
        period = period.plus(Period.days(periodBean.days));
        period = period.plus(Period.hours(periodBean.hours));
        period = period.plus(Period.minutes(periodBean.minutes));
        period = period.plus(Period.seconds(periodBean.seconds));
        return period;
    }

    private static QueryBuilder getFilter(List filter) {
        Gson gson = new Gson();
        HashMap<String, Object> filters = new HashMap<>();
        filters.put("filter", filter);
        HashMap<String, Map> boolQuery = new HashMap<>();
        boolQuery.put("bool", filters);
        String string = gson.toJson(boolQuery);
        return QueryBuilders.wrapperQuery(string);
    }
}
