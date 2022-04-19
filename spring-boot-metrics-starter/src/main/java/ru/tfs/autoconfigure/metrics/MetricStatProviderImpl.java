package ru.tfs.autoconfigure.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;


@Component
public class MetricStatProviderImpl implements MetricStatProvider {
    private final MetricStatService metricStatService;

    @Autowired
    public MetricStatProviderImpl(MetricStatService metricStatService) {
        this.metricStatService = metricStatService;
    }

    @Override
    public List<MethodMetricStat> getTotalStat() {
        ConcurrentMap<String, List<MethodInvocationMetric>> data = metricStatService.getData();
        List<MethodMetricStat> metricStatList = new ArrayList<>();
        data.forEach((key, value) -> {
            int max = value.get(0).getTotalTime();
            int min = value.get(0).getTotalTime();;
            int count = 0;
            int sum = 0;
            for (MethodInvocationMetric methodInvocationMetric : value) {
                int current = methodInvocationMetric.getTotalTime();
                sum += current;
                count++;
                if (current > max) {
                    max = current;
                }
                if (current < min) {
                    min = current;
                }
            }

            metricStatList.add(new MethodMetricStat(key, value.size(), min, sum / count, max));
        });



        return metricStatList;
    }

    @Override
    public List<MethodMetricStat> getTotalStatForPeriod(LocalDateTime from, LocalDateTime to) {
        ConcurrentMap<String, List<MethodInvocationMetric>> data = metricStatService.getData();
        List<MethodMetricStat> metricStatList = new ArrayList<>();
        data.forEach((key, value) -> {
            int max = value.get(0).getTotalTime();
            int min = value.get(0).getTotalTime();;
            int count = 0;
            int sum = 0;
            for (MethodInvocationMetric methodInvocationMetric : value) {
                if (methodInvocationMetric.getInvocationTime().isBefore(to) &&
                    methodInvocationMetric.getInvocationTime().isAfter(from)) {
                    int current = methodInvocationMetric.getTotalTime();
                    sum += current;
                    count++;
                    if (current > max) {
                        max = current;
                    }
                    if (current < min) {
                        min = current;
                    }
                }
            }

            metricStatList.add(new MethodMetricStat(key, value.size(), min, sum / count, max));
        });

        return metricStatList;
    }

    @Override
    public MethodMetricStat getTotalStatByMethod(String method) {
        List<MethodInvocationMetric> data = metricStatService.getDataByMethodName(method);

        int max = data.get(0).getTotalTime();
        int min = data.get(0).getTotalTime();;
        int count = 0;
        int sum = 0;
        for (MethodInvocationMetric methodInvocationMetric : data) {
            int current = methodInvocationMetric.getTotalTime();
            sum += current;
            count++;
            if (current > max) {
                max = current;
            }
            if (current < min) {
                min = current;
            }
        }

        return new MethodMetricStat(method, data.size(), min,sum / count, max);
    }

    @Override
    public MethodMetricStat getTotalStatByMethodForPeriod(String method, LocalDateTime from, LocalDateTime to) {
        List<MethodInvocationMetric> data = metricStatService.getDataByMethodName(method);

        int max = data.get(0).getTotalTime();
        int min = data.get(0).getTotalTime();;
        int count = 0;
        int sum = 0;
        for (MethodInvocationMetric methodInvocationMetric : data) {
            if (methodInvocationMetric.getInvocationTime().isBefore(to) &&
                    methodInvocationMetric.getInvocationTime().isAfter(from)) {
                int current = methodInvocationMetric.getTotalTime();
                sum += current;
                count++;
                if (current > max) {
                    max = current;
                }
                if (current < min) {
                    min = current;
                }
            }
        }

        return new MethodMetricStat(method, data.size(), min,sum / count, max);
    }
}
