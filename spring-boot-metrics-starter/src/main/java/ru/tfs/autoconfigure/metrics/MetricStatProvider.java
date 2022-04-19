package ru.tfs.autoconfigure.metrics;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис предоставления метрик
 */
public interface MetricStatProvider {

    /**
     * Получить статистику по метрикам по всем методам
     */
    List<MethodMetricStat> getTotalStat();

    /**
     * Получить статистику по метрикам по всем методам за указанный период
     */
    List<MethodMetricStat> getTotalStatForPeriod(LocalDateTime from,
                                                 LocalDateTime to);

    /**
     * Получить статистику по метрикам по указанному методу
     */
    MethodMetricStat getTotalStatByMethod(String method);

    /**
     * Получить статистику по метрикам по указанному методу за указанный период
     */
    MethodMetricStat getTotalStatByMethodForPeriod(String method,
                                                   LocalDateTime from,
                                                   LocalDateTime to);
}
