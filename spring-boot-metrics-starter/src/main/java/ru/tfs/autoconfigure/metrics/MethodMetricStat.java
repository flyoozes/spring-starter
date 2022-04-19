package ru.tfs.autoconfigure.metrics;

/**
 * Статистика метрик метода
 */
public class MethodMetricStat {

    /**
     * Наименование/идентификатор метода
     */
    private String methodName;
    /**
     * Кол-во вызовов метода
     */
    private Integer invocationsCount;
    /**
     * Минимальное время работы метода
     */
    private Integer minTime;
    /**
     * Среднее время работы метода
     */
    private Integer averageTime;
    /**
     * максимальное время работы метода
     */
    private Integer maxTime;

    public MethodMetricStat(String methodName, Integer invocationsCount, Integer minTime, Integer averageTime, Integer maxTime) {
        this.methodName = methodName;
        this.invocationsCount = invocationsCount;
        this.minTime = minTime;
        this.averageTime = averageTime;
        this.maxTime = maxTime;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getInvocationsCount() {
        return invocationsCount;
    }

    public void setInvocationsCount(Integer invocationsCount) {
        this.invocationsCount = invocationsCount;
    }

    public Integer getMinTime() {
        return minTime;
    }

    public void setMinTime(Integer minTime) {
        this.minTime = minTime;
    }

    public Integer getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(Integer averageTime) {
        this.averageTime = averageTime;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }
}
