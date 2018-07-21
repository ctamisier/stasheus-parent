package io.stasheus;

import java.util.HashMap;
import java.util.Map;

public class Metrics {

    private Map<String, Object> metrics;

    public Metrics() {
        metrics = new HashMap<>();
    }

    public void addMetric(Metric metric) {
        metrics.put(metric.getKey(), metric.getValue());
    }

    public Map<String, Object> asMap() {
        return metrics;
    }
}
