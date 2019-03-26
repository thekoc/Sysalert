package xyz.thekoc.sysalert;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PostAgentTest {
    PostAgent post = new PostAgent("localhost", 9200, "http");

    @Test
    public void initIndex() {
        try {
            post.initIndex("sysalert-test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void postData() {

        try {
            while (true) {
                for (int i = 0; i < 10; i++) {
                    post.postData();
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}