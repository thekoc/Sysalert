package xyz.thekoc.sysalert;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MonitoredEventTypes implements Iterable<MonitoredEventType> {
    private ArrayList<MonitoredEventType> monitoredEventTypes;

    public MonitoredEventTypes() {
        monitoredEventTypes = new ArrayList<>();
    }

    public MonitoredEventTypes(List<MonitoredEventType> monitoredEventTypes) {
        this.monitoredEventTypes = new ArrayList<>(monitoredEventTypes);
    }

    public void addFilterToAll(QueryBuilder filter) {
        for (MonitoredEventType eventType: monitoredEventTypes) {
            eventType.setFilter(new BoolQueryBuilder().filter(eventType.getFilter()).filter(filter));
        }
    }

    public void add(MonitoredEventType monitoredEventType) {
        monitoredEventTypes.add(monitoredEventType);
    }

    public ArrayList<MonitoredEventType> getMonitoredEventTypes() {
        return monitoredEventTypes;
    }

    @Override
    public Iterator<MonitoredEventType> iterator() {
        return monitoredEventTypes.listIterator();
    }
}
