package xyz.thekoc.sysalert.conifg;

import java.util.Map;

public class PeriodBean {
    PeriodBean() {}

    PeriodBean(Map<String, String> object) {
        if (object != null) {
            days = Integer.valueOf(object.getOrDefault("days", "0"));
            hours = Integer.valueOf(object.getOrDefault("hours", "0"));
            minutes = Integer.valueOf(object.getOrDefault("minutes", "0"));
            seconds = Integer.valueOf(object.getOrDefault("seconds", "0"));
        }
    }

    public int days = 0;
    public int hours = 0;
    public int minutes = 0;
    public int seconds = 0;
}
