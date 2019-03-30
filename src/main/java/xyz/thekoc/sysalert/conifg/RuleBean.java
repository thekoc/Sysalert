package xyz.thekoc.sysalert.conifg;

import java.util.List;
import java.util.Map;

public class RuleBean {
    public String type;
    public String name;
    public Map<String, Number> timewindow;
    public List<Map> filter;
}
