package xyz.thekoc.sysalert.alert;

import xyz.thekoc.sysalert.MatchedEvent;
import xyz.thekoc.sysalert.PostAgent;
import xyz.thekoc.sysalert.conifg.Config;

import java.io.IOException;
import java.util.List;

public class ElasticsearchAlerter extends Alerter{
    private Config config = Config.getConfig();
    private PostAgent postAgent = new PostAgent(config.getHostname(), config.getPort(), config.getScheme());

    @Override
    void alert(List<MatchedEvent> matchedEvents) throws IOException {
        postAgent.postData("sysalert-alert", "_doc", "abcdefg");
    }
}
