package ru.tfs.autoconfigure.metrics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MetricStatService {

    public static ConcurrentMap<String, List<MethodInvocationMetric>> concurrentMap = new ConcurrentHashMap<>();
    @Value("${metrics.limit}")
    private Integer limit;

    public void addStat(MethodInvocationMetric metric) {
        concurrentMap.compute(metric.getMethod(), (k, v) -> {
            LinkedList<MethodInvocationMetric> values = (LinkedList<MethodInvocationMetric>) v;
            if (values == null) {
                values = new LinkedList<>();
            }
            if (values.size() >= limit) {
                values.removeFirst();
            }
            values.add(metric);
            return values;
        });
    }

    public ConcurrentMap<String, List<MethodInvocationMetric>> getData() {
        return concurrentMap;
    }

    public List<MethodInvocationMetric> getDataByMethodName(String name) {
        return concurrentMap.get(name);
    }

    public void clear() {
        concurrentMap.clear();;
    }

}
