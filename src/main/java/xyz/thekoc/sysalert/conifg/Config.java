package xyz.thekoc.sysalert.conifg;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Config {
    private ConfigBean configBean;
    private static Config configSingletonInstance = null;

    private Config(String pathname) throws FileNotFoundException {
        YamlReader reader = new YamlReader(new FileReader(pathname));
        try {
            configBean = reader.read(ConfigBean.class);
        } catch (YamlException e) {
            e.printStackTrace();
        }
    }

    public static void init(String pathname) throws FileNotFoundException {
        configSingletonInstance = new Config(pathname);
    }

    public static Config getConfig() {
        assert configSingletonInstance != null;
       return configSingletonInstance;
    }

    public String getHostname() {
        return configBean.es_host;
    }

    public int getPort() {
        return configBean.es_port;
    }

    public String getScheme() {
        return configBean.scheme;
    }
}
