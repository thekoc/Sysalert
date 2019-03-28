package xyz.thekoc.sysalert;

import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;

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
        (new Thread(() ->
        {
//            try {
//                Thread.sleep(10000);
//                new PostAgent("localhost", 9200, "http").
//                        postData("sysalert-test", "_doc", "event_id", 4, "@timestamp", DateTime.now(), "source_name", "Microsoft-Windows-Sysmon");
//                System.out.println("posted");
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        })).start();
        try {
            int totalPosted = 0;
            while (true) {
                for (int i = 0; i < 10; i++) {
                    totalPosted += 1;
                    post.postData("sysalert-test", "_doc",
                            "event_id", 3,
                            "@timestamp", DateTime.now(),
                            "message", "trying out Elasticsearch",
                            "source_name", "Microsoft-Windows-Sysmon");
                }
//                System.out.println("totalPosted: " + totalPosted);
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}