package xyz.thekoc.sysalert.conifg;

import java.util.List;
import java.util.Map;

public class RuleBean {
    public String type;
    public String name;
    public String index = null;
    public int num_events;
    public TimeWindowBean timewindow;
    public List<Map> filter;

    static public RuleBean fromFile(String pathname) {
        return null;
    }
}
