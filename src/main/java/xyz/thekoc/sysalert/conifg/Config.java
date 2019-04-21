package xyz.thekoc.sysalert.conifg;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import xyz.thekoc.sysalert.rule.RuleType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Config {
    private ConfigBean configBean = new ConfigBean();
    private ArrayList<RuleType> ruleTypes = new ArrayList<>();
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

    public RuleType addRule(String pathname) throws FileNotFoundException, YamlException, FieldMissingException, NoSuchRuleException, FieldValueException {
        RuleType rule = RuleBuilder.fromFile(pathname);
        ruleTypes.add(rule);
        return rule;
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
        return ruleTypes;
    }
}
