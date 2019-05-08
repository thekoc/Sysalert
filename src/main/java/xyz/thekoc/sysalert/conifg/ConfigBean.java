package xyz.thekoc.sysalert.conifg;

public class ConfigBean {
    public String es_host = "localhost";
    public int es_port = 9200;
    public String scheme = "http";
    public String rules_folder = null;
    public PeriodBean run_every = new PeriodBean(0, 0, 0, 10, 0);
    public String index = null;
}
