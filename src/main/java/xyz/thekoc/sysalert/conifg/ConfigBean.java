package xyz.thekoc.sysalert.conifg;

import java.util.Map;

public class ConfigBean {
    public String es_host = "localhost";
    public int es_port = 9200;
    public String scheme = "http";
    public String rules_folder = null;
    public PeriodBean run_every = new PeriodBean(0, 0, 0, 10, 0);
    public String index = null;

    ConfigBean() {

    }

    @SuppressWarnings("unchecked")
    ConfigBean(Map object) {
        if (object.get("es_host") != null) {
            es_host = (String) object.get("es_host");
        }
        if (object.get("es_port") != null) {
            es_port = Integer.valueOf((String) object.get("es_port"));
        }
        if (object.get("scheme") != null) {
            scheme = (String) object.get("scheme");
        }
        if (object.get("rules_folder") != null) {
            rules_folder = (String) object.get("rules_folder");
        }
        if (object.get("run_every") != null) {
            run_every = new PeriodBean((Map) object.get("run_every"));
        }
        index = (String) object.get("index");
    }
}
