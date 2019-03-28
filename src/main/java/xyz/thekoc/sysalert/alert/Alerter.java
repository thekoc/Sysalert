package xyz.thekoc.sysalert.alert;

import xyz.thekoc.sysalert.MatchedEvent;

import java.io.IOException;
import java.util.List;

public abstract class Alerter {
    abstract void alert(List<MatchedEvent> matchedEvents) throws IOException;
}
