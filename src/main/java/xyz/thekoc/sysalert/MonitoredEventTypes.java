package xyz.thekoc.sysalert;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MonitoredEventTypes implements Iterable<MonitoredEventType> {
    private ArrayList<MonitoredEventType> monitoredEventTypes;

    public MonitoredEventTypes() {
        monitoredEventTypes = new ArrayList<>();
    }

    public MonitoredEventTypes(List<MonitoredEventType> monitoredEventTypes) {
        this.monitoredEventTypes = new ArrayList<>(monitoredEventTypes);
    }

    public MonitoredEventTypes(String index, QueryBuilder... filters) {
        this(Arrays.stream(filters).map((f) -> {
            return new MonitoredEventType(index, f);
        }).collect(Collectors.toList()));
    }

    public void addFilterToAll(QueryBuilder filter) {
        if (filter == null) {
            return;
        }
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

    public MonitoredEventType get(int index) {
        return monitoredEventTypes.get(index);
    }

    public int size() {
        return monitoredEventTypes.size();
    }

    public boolean contains(MonitoredEventType eventType) {
        return monitoredEventTypes.contains(eventType);
    }
}
