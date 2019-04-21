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
    public static void verifyRuleBean(RuleBean ruleBean) throws FieldMissingException, FieldValueException {
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
            case "compound":
                if (ruleBean.operator == null) {
                    ruleBean.operator = "and";
                }
                if (!Arrays.asList("and", "or").contains(ruleBean.operator)) {
                    throw new FieldValueException();
                }

                if (ruleBean.rules == null) {
                    throw new FieldMissingException();
                } else {
                    for (Map rule: ruleBean.rules) {
                        RuleBean subBean = new RuleBean(rule);
                        verifyRuleBean(subBean);
                    }
                }
                break;
        }
    }

    private static RuleType fromYamlObject(Map rawYamlObject) throws FieldMissingException, NoSuchRuleException, FieldValueException {
        Config config = Config.getConfig();
        String index = rawYamlObject.get("index") != null ? (String) rawYamlObject.get("index") : config.getIndex();
        String type = (String) rawYamlObject.get("type");
        if (type == null) {
            throw new FieldMissingException("type field cannot be null!");
        } else if (Arrays.asList("frequency", "sysmon_frequency", "combination", "sequence", "blacklist").contains(type)) {
            RuleBean ruleBean = new RuleBean(rawYamlObject);

            verifyRuleBean(ruleBean);

            Period timeWindow = periodFromPeriodBean(ruleBean.timewindow);
            Period queryDelay = periodFromPeriodBean(ruleBean.query_delay);
            RuleType newRule;

            switch (ruleBean.type) {
                case "blacklist":
                    newRule = new BlacklistRule(index, getFilter(ruleBean.filter));
                    break;
                case "whitelist":
                    newRule = new WhitelistRule(index, getFilter(ruleBean.filter));
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
                        combinationRuleTypes.add(new MonitoredEventType(index, getFilter(Collections.singletonList(filter))));
                    }
                    combinationRuleTypes.addFilterToAll(getFilter(ruleBean.filter));
                    newRule = new CombinationRule(timeWindow, combinationRuleTypes);
                    break;
                case "sequence":
                    MonitoredEventTypes sequenceRuleTypes = new MonitoredEventTypes();
                    for (Map filter: ruleBean.sequence) {
                        sequenceRuleTypes.add(new MonitoredEventType(index, getFilter(Collections.singletonList(filter))));
                    }
                    sequenceRuleTypes.addFilterToAll(getFilter(ruleBean.filter));
                    newRule = new CombinationRule(timeWindow, sequenceRuleTypes);
                    break;
                case "compound":
                    ArrayList<RuleType> rules = new ArrayList<>();
                    for (Map rule: ruleBean.rules) {
                        rules.add(fromYamlObject(rule));
                    }
                    newRule = new CompoundRule(CompoundRule.Operator.AND, rules.toArray(new RuleType[0]));
                    break;

                default:
                    throw new NoSuchRuleException("No such rule: " + ruleBean.type);
            }

            newRule.setQueryDelayForAll(queryDelay);

            return newRule;
        }
        return null;
    }

    static RuleType fromFile(String pathname) throws FileNotFoundException, YamlException, FieldMissingException, NoSuchRuleException, FieldValueException {
        YamlReader rawReader = new YamlReader(new FileReader(pathname));
        return fromYamlObject((Map) rawReader.read());
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
