package xyz.thekoc.sysalert.conifg;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.apache.commons.cli.*;
import org.joda.time.Period;
import xyz.thekoc.sysalert.rule.RuleType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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

    public static void init(String[] argv) throws FileNotFoundException, FieldValueException, YamlException, NoSuchRuleException, FieldMissingException {
        Options options = new Options();

        Option configPathOption = Option.builder("c").longOpt("config").required().
                desc("Config path").build();
        options.addOption(configPathOption);

        Option rulePathOption = Option.builder("r").longOpt("rule").hasArgs().desc("Rule path").build();
        options.addOption(rulePathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, argv);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }

        String configPath = cmd.getOptionValue("config");
        String[] rulePaths = cmd.getOptionValues("rule");
        configSingletonInstance = new Config(configPath);
        for (String rulePath: rulePaths) {
            configSingletonInstance.addRule(rulePath);
        }
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

    public Period getRunEvery() {
        PeriodBean runEvery = configBean.run_every;
        return new Period(runEvery.hours, runEvery.minutes, runEvery.seconds, 0);
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
