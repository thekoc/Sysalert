package xyz.thekoc.sysalert.conifg;

import com.esotericsoftware.yamlbeans.YamlException;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import xyz.thekoc.sysalert.SearchAgent;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String rulepath = classLoader.getResource("test_rule.yml").getFile();
        try {
            config.addRule(rulepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (YamlException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
//        String string = gson.toJson(config.getRuleBeans().get(0).filter);
        HashMap<String, List> filters = new HashMap<String, List>();
        filters.put("filter", config.getRuleBeans().get(0).filter);
        HashMap<String, Map> boolQuery = new HashMap<>();
        boolQuery.put("bool", filters);
        String string = gson.toJson(boolQuery);

        QueryBuilder q = QueryBuilders.wrapperQuery(string);
        SearchAgent searchAgent = new SearchAgent("localhost", 9200, "http");
        SearchResponse response = searchAgent.search("sysalert-*", q);
        System.out.println(response);
    }
}