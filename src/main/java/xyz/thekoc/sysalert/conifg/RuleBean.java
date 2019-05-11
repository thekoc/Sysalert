package xyz.thekoc.sysalert.conifg;

import java.util.List;
import java.util.Map;

public class RuleBean {
    @SuppressWarnings("unchecked")
    RuleBean(Map object) {
        type = (String) object.get("type");
        name = (String) object.get("name");
        index = (String) object.get("index");
        if (object.get("num_events") != null) {
            num_events = Integer.valueOf((String) object.get("num_events"));
        } else {
            num_events = null;
        }
        if (object.get("timewindow") != null) {
            timewindow = new PeriodBean((Map) object.get("timewindow"));
        } else {
            timewindow = null;
        }
        if (object.get("query_delay") != null) {
            query_delay = new PeriodBean((Map) object.get("query_delay"));
        } else {
            query_delay = null;
        }
        filter = (List<Map>) object.get("filter");
        blacklist = (List<Map>) object.get("blacklist");
        sequence = (List<Map>) object.get("sequence");
        combination = (List<Map>) object.get("combination");
        operator = (String) object.get("operator");
        if (operator != null) {
            operator = operator.toLowerCase();
        }
        rules = (List<Map>) object.get("rules");

    }

    public String type;
    public String name;
    public String index;
    public Integer num_events;
    public PeriodBean timewindow;
    public PeriodBean query_delay;
    public List<Map> filter;
    public List<Map> blacklist;
    public List<Map> sequence;
    public List<Map> combination;
    public String operator;
    public List<Map> rules;
}
