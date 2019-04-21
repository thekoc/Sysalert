package xyz.thekoc.sysalert.alert;

import xyz.thekoc.sysalert.rule.RuleHit;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class PopupAlerter extends Alerter {
    private LinkedList<Integer> poped = new LinkedList<>();

    @Override
    public void alert(RuleHit ruleHit) {
        // TODO: Popup real window
//        String[] cmd = {"osascript", "-e", "'display dialog \"test\"'"};
        String[] cmd = { "osascript", "-e", String.format("display dialog \"%s\" with title \"%s\"", ruleHit.message, ruleHit.ruleType)};
        if (!poped.contains(Arrays.hashCode(cmd))) {
            poped.add(Arrays.hashCode(cmd));
            try {
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
