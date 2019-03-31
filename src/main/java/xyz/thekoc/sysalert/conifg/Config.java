package xyz.thekoc.sysalert.conifg;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import xyz.thekoc.sysalert.rule.RuleType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    private ConfigBean configBean = new ConfigBean();
    private ArrayList<RuleBean> ruleBeans= new ArrayList<RuleBean>();
    private static Config configSingletonInstance = null;

    private Config(String pathname) throws FileNotFoundException {
        YamlReader reader = new YamlReader(new FileReader(pathname));
        try {
            configBean = reader.read(ConfigBean.class);
        } catch (YamlException e) {
            e.printStackTrace();
        }
    }

    private Config() {
    }

    public static void init(String pathname) throws FileNotFoundException {
        configSingletonInstance = new Config(pathname);
    }

    public static Config getConfig() {
        if (configSingletonInstance == null) {
            configSingletonInstance = new Config();
        }
       return configSingletonInstance;
    }

    public void addRule(String pathname) throws FileNotFoundException, YamlException {
        YamlReader reader = new YamlReader(new FileReader(pathname));
        reader.getConfig().setPropertyDefaultType(RuleBean.class, "timewindow", TimeWindowBean.class);
        RuleBean ruleBean = reader.read(RuleBean.class);
        ruleBeans.add(ruleBean);
    }

    public ArrayList<RuleBean> getRuleBeans() {
        return ruleBeans;
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

    public String getIndex() {
        return configBean.index;
    }

    public List<RuleType> getRuleTypes() {
        return ruleBeans.stream().map(RuleBuilder::fromRuleBean).collect(Collectors.toList());
    }
}
