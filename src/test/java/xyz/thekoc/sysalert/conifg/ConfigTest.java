package xyz.thekoc.sysalert.conifg;

import com.esotericsoftware.yamlbeans.YamlException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class ConfigTest {

    @Test
    public void getConfig() {
        ClassLoader classLoader = getClass().getClassLoader();
        String pathname = classLoader.getResource("test_config.yml").getFile();
        try {
            Config.init(pathname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Config config = Config.getConfig();
        String rulepath = classLoader.getResource("frequency_rule.yml").getFile();
        try {
            config.addRule(rulepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (YamlException e) {
            e.printStackTrace();
        }
//        QueryBuilders.wrapperQuery(JSON)
    }
}