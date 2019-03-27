package xyz.thekoc.sysalert;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;

public class PostAgentTest {
    PostAgent post = new PostAgent("localhost", 9200, "http");

    @Test
    public void initIndex() {
        try {
            post.initIndex("sysalert-test", "_doc",
                    "message", "type=text", "@timestamp", "type=date",
                    "event_id", "type=integer", "source_name", "type=text");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void postData() {

        try {
            while (true) {
                for (int i = 0; i < 10; i++) {
                    post.postData("sysalert-test", "_document",
                            "event_id", 3,
                            "@timestamp", new Date(),
                            "message", "trying out Elasticsearch",
                            "source_name", "Microsoft-Windows-Sysmon");
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}