package xyz.thekoc.sysalert.rule;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.*;

public class RuleTypeTest {

    @Test
    public void getDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTime dt = dateTimeFormatter.parseDateTime("2019-03-16T14:56:46.397Z");

        DateTime x = DateTime.now();
        System.out.println(dt);
    }
}