package xyz.thekoc.sysalert.agent;

import org.joda.time.DateTime;
import org.junit.Test;
import xyz.thekoc.sysalert.agent.PostAgent;

import java.io.IOException;

public class PostAgentTest {
    PostAgent post = new PostAgent("localhost", 9200, "http");

    @Test
    public void initIndex() {
        System.out.println(123);
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

    public void generateEvent(int interval, int eventPerTime, int eventId) {
        generateEvent(interval, eventPerTime, eventId, 0);
    }

    @Test
    public void generateEvent(int interval, int eventPerTime, int eventId, int wait) {
        new Thread(() ->
        {
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    post.postData("sysalert-test", "_doc",
                            "event_id", eventId,
                            "@timestamp", DateTime.now(),
                            "message", "trying out Elasticsearch",
                            "source_name", "Microsoft-Windows-Sysmon");
                    Thread.sleep(interval / eventPerTime);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }).start();
    }
}