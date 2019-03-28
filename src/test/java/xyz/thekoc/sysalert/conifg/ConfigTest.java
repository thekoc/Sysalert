package xyz.thekoc.sysalert.conifg;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class ConfigTest {

    @Test
    public void getConfig() {
        ClassLoader classLoader = getClass().getClassLoader();
        String pathname = classLoader.getResource("default_config.yml").getFile();
        try {
            Config.init(pathname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Config config = Config.getConfig();
        System.out.println(config.getHostname());
    }
}