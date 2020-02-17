# Sysalert
An Elasticsearch plugin that helps you monitor specified events
Set different rules to monitor suspicious events in Elasticsearch with Sysmon.

## Example config files for new rule

### BlackList
Alert when specified events appear.
```yaml
name: blacklist test
type: blacklist
blacklist:
  - filter:
      - term:
          event_id: 11
      - regexp:
          event_data.TargetFilename.keyword: .*xxxyyy.*
index: sysalert-*
```

### Change
Alert when some constant value change.
```yaml
name: change test
type: change
field: "age"
index: sysalert-*
```

### Frequency
Alert when some events occurs too frequent. 
```yaml
name: frequency test
type: frequency
timewindow:
  seconds: 10
filter:
  - term:
      event_id: 3
num_events: 2000
index: sysalert-*
```

### Spike
Alert when some events' occurrence increases over a certain threshold.
```yaml
name: spike test
type: spike
timewindow:
  seconds: 10
filter:
  - term:
      event_id: 3
spike_height: 2000
index: sysalert-*
```

### New Term
Alert when new values occur in a specified field.
```yaml
name: new term test
type: new term
timewindow:
  seconds: 10
filter:
  - term:
      event_id: 3
field: "age"
index: sysalert-*
```

### Sequence
Alert when events occur in a specified sequence.
```yaml
name: sequence test
type: sequence
sequence:
  - filter:
    - term:
       	age: 20
  - filter:
    - term:
        age: 25
timewindow:
  seconds: 10
index: students
```

### Combination
Alert when specified events both occur in a timewindow.
```yaml
name: combination test
type: combination
combination:
  - filter:
    - term:
        name: Harry Potter
  - filter:
    - term:
        name: Albus Percival Wulfric Brian Dumbledore
timewindow:
  hours: 5
index: Gryffindor
```

### Compound
A compound rule that is able to include different rules.
```yaml
type: compound
operator: and
rules:
  - name: frequency rule test
    type: frequency
    timewindow:
      seconds: 10
    filter:
      - term:
          event_id: 3
    num_events: 2000
    index: sysalert-*
  - name: combination rule test
    type: combination
    timewindow:
      seconds: 10
    combination:
      - term:
          event_id: 3
      - term:
          event_id: 8
    index: sysalert-*
```
