package xyz.thekoc.sysalert.conifg;

import java.util.List;
import java.util.Map;

public class RuleBean {
    public String type;
    public String name;
    public String index = null;
    public Integer num_events;
    public TimeWindowBean timewindow;
    public List<Map> filter;
    public List<Map> event_sequence;
    public List<Map> event_combination;
}
