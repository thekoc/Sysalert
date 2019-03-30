package xyz.thekoc.sysalert;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

public class SearchAgent {
    private RestHighLevelClient client;
    private int querySize;
    public SearchAgent(String hostname, Integer port, String scheme) {
         client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostname, port, scheme)));
         querySize = 10000;
    }

    public SearchResponse search(String index, QueryBuilder query) {
        return search(index, query, null);
    }

    public SearchResponse search(String index, QueryBuilder query, SortBuilder sort) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.size(querySize);
        searchSourceBuilder.query(query);

        if (sort != null) {
            searchSourceBuilder.sort(sort);
        }
        searchRequest.source(searchSourceBuilder);

        try {
            return client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() throws IOException {
        client.close();
    }

}
