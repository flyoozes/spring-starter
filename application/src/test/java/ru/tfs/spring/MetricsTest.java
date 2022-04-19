package ru.tfs.spring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.tfs.autoconfigure.metrics.MethodMetricStat;
import ru.tfs.autoconfigure.metrics.MetricStatProvider;
import ru.tfs.autoconfigure.metrics.MetricStatService;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MetricsTest {
    FirstSampleService firstSampleService;
    SecondSampleService secondSampleService;
    MetricStatProvider metricStatProvider;
    MetricStatService metricStatService;

    @Autowired
    public MetricsTest(FirstSampleService firstSampleService,
                       SecondSampleService secondSampleService,
                       MetricStatProvider metricStatProvider,
                       MetricStatService metricStatService
    ) {
        this.firstSampleService = firstSampleService;
        this.secondSampleService = secondSampleService;
        this.metricStatProvider = metricStatProvider;
        this.metricStatService = metricStatService;
    }

    @AfterEach
    void init() {
        metricStatService.clear();
    }

    @Test
    public void whenClassAnnotatedThenTimedAllMethods() {
        ThreadLoopFirstMethod threadLoopFirstMethod = new ThreadLoopFirstMethod(firstSampleService);
        ThreadLoopSecondMethod threadLoopSecondMethod = new ThreadLoopSecondMethod(firstSampleService);
        threadLoopFirstMethod.run();
        threadLoopSecondMethod.run();

        List<MethodMetricStat> data = metricStatProvider.getTotalStat();
        List<String> expected = List.of("justNothing", "justAnotherNothing");
        List<String> methodNames = data.stream().map(MethodMetricStat::getMethodName).collect(Collectors.toList());

        Assertions.assertTrue(methodNames.containsAll(expected));
    }

    @Test
    public void allMethodsCounted() {
        ThreadLoopFirstMethod threadLoopFirstMethod = new ThreadLoopFirstMethod(firstSampleService);
        ThreadLoopSecondMethod threadLoopSecondMethod = new ThreadLoopSecondMethod(firstSampleService);
        threadLoopFirstMethod.run();
        threadLoopSecondMethod.run();

        List<MethodMetricStat> data = metricStatProvider.getTotalStat();
        List<Integer> expected = List.of(10, 50);
        List<Integer> invocationTimes = data.stream().map(MethodMetricStat::getInvocationsCount).collect(Collectors.toList());
        Assertions.assertTrue(invocationTimes.containsAll(expected));
    }

    @Test
    public void countedNotMoreThanLimit() {
        ThreadLoopSecondMethod threadLoopSecondMethod = new ThreadLoopSecondMethod(firstSampleService);
        threadLoopSecondMethod.run();

        MethodMetricStat data = metricStatProvider.getTotalStatByMethod("justAnotherNothing");

        assertEquals(data.getInvocationsCount(), 50);
    }

    @Test
    public void countOnlyTimedMethod() {
        ThreadLoopSecondMethod threadLoopSecondMethod = new ThreadLoopSecondMethod(secondSampleService);
        threadLoopSecondMethod.run();

        List<MethodMetricStat> data = metricStatProvider.getTotalStat();
        List<String> methodNames = data.stream().map(MethodMetricStat::getMethodName).collect(Collectors.toList());
        List<String> expected = List.of("justAnotherNothing");

        assertEquals(methodNames, expected);
    }
}
