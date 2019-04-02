package xyz.thekoc.sysalert.alert;

import xyz.thekoc.sysalert.agent.PostAgent;
import xyz.thekoc.sysalert.conifg.Config;
import xyz.thekoc.sysalert.rule.RuleHit;


public class ElasticsearchAlerter extends Alerter{
    private Config config = Config.getConfig();
    private PostAgent postAgent = new PostAgent(config.getHostname(), config.getPort(), config.getScheme());

    @Override
    public void alert(RuleHit ruleHit) {

    }
}
