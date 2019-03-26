package xyz.thekoc.sysalert;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

public class SearchAgentTest {

    @Test
    public void search() {
        SearchAgent searchAgent = new SearchAgent("localhost", 9200, "http");


        QueryBuilder eventQuery = QueryBuilders.termQuery("event_id", 3);
        QueryBuilder sourceQuery = QueryBuilders.termQuery("source_name.keyword", "Microsoft-Windows-Sysmon");
        QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp").gte("now-15d/d").lte("now/d");
        QueryBuilder boolQuery = QueryBuilders.boolQuery().filter(sourceQuery).filter(eventQuery);
        QueryBuilder finalQuery = QueryBuilders.boolQuery().filter(rangeQueryBuilder).filter(boolQuery);

        SearchResponse response = searchAgent.search("winlogbeat-*", finalQuery, null);
        int i = 0;
        for (SearchHit hit: response.getHits()) {
            i += 1;
        }
        System.out.println(i);
    }
}