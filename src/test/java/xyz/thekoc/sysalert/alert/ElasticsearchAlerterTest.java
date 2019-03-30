package xyz.thekoc.sysalert.alert;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ElasticsearchAlerterTest {

    ElasticsearchAlerter elasticsearchAlerter = new ElasticsearchAlerter();

    @Test
    public void alert() {
        try {
            elasticsearchAlerter.alert(new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}