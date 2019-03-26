package xyz.thekoc.sysalert;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Date;

public class PostAgent {
    private RestHighLevelClient client;
    public PostAgent(String hostname, Integer port, String scheme) {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostname, port, scheme)));
    }

    public void initIndex(String name) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(name);
        request.mapping("_doc", "message", "type=text", "@timestamp", "type=date", "event_id", "type=integer", "source_name", "type=text");
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
    }

    public void postData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("sysalert-test", "_doc")
                .source("event_id", 3,
                        "@timestamp", new Date(),
                        "message", "trying out Elasticsearch",
                        "source_name", "Microsoft-Windows-Sysmon");
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
    }

}
