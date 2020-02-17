# Sysalert
An Elasticsearch plugin that helps you monitor specified events
Set different rules to monitor suspicious events in Elasticsearch with Sysmon.

## Example config file for new rule
```yaml
name: frequency rule test
type: frequency
timewindow:
  seconds: 10
filter:
  - term:
      event_id: 3
num_events: 2000
index: sysalert-*
```
