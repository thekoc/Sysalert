package xyz.thekoc.sysalert.conifg;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.gson.Gson;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.Period;
import xyz.thekoc.sysalert.MonitoredEventType;
import xyz.thekoc.sysalert.MonitoredEventTypes;
import xyz.thekoc.sysalert.rule.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

class RuleBuilder {
    static RuleType fromFile(String pathname) throws FileNotFoundException, YamlException, FieldMissingException {
        YamlReader rawReader = new YamlReader(new FileReader(pathname));
        Map rawYamlObject = (Map) rawReader.read();

        Config config = Config.getConfig();
        String index = rawYamlObject.get("index") != null ? (String) rawYamlObject.get("index") : config.getIndex();
        String type = (String) rawYamlObject.get("type");
        if (type == null) {
            throw new YamlException("type field cannot be null!");
        } else if (Arrays.asList("frequency", "sysmon_frequency", "combination", "sequence", "blacklist").contains(type)) {
            YamlReader reader = new YamlReader(new FileReader(pathname));
            reader.getConfig().setPropertyDefaultType(RuleBean.class, "timewindow", TimeWindowBean.class);
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

            Period timeWindow = Period.ZERO;
            TimeWindowBean timeWindowBean = ruleBean.timewindow;
            timeWindow.plus(Period.days(timeWindowBean.days));
            timeWindow.plus(Period.hours(timeWindowBean.hours));
            timeWindow.plus(Period.minutes(timeWindowBean.minutes));
            timeWindow.plus(Period.seconds(timeWindowBean.seconds));

            switch (ruleBean.type) {
                case "blacklist":
                    return new BlacklistRule(index, getFilter(ruleBean.filter));
                case "frequency":
                    return new FrequencyRule(index, timeWindow, getFilter(ruleBean.filter), ruleBean.num_events);
                case "sysmon_frequency":
                    return new SysmonFrequencyRule(index, timeWindow, getFilter(ruleBean.filter), ruleBean.num_events);
                case "combination":
                    MonitoredEventTypes combinationRuleTypes = new MonitoredEventTypes();
                    for (Map filter: ruleBean.combination) {
                        combinationRuleTypes.add(new MonitoredEventType(getFilter(Collections.singletonList(filter))));
                    }
                    combinationRuleTypes.addFilterToAll(getFilter(ruleBean.filter));
                    return new CombinationRule(index, timeWindow, combinationRuleTypes);
                case "sequence":
                    MonitoredEventTypes sequenceRuleTypes = new MonitoredEventTypes();
                    for (Map filter: ruleBean.sequence) {
                        sequenceRuleTypes.add(new MonitoredEventType(getFilter(Collections.singletonList(filter))));
                    }
                    sequenceRuleTypes.addFilterToAll(getFilter(ruleBean.filter));
                    return new CombinationRule(index, timeWindow, sequenceRuleTypes);
            }
        }
        return null;
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
