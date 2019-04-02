package xyz.thekoc.sysalert.alert;

import org.junit.Test;

import static org.junit.Assert.*;

public class PopupAlerterTest {

    @Test
    public void alert() {
        new PopupAlerter().alert(null);
    }
}