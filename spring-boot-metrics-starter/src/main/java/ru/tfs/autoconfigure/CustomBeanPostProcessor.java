package ru.tfs.autoconfigure;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.tfs.autoconfigure.annotations.Timed;
import ru.tfs.autoconfigure.metrics.MethodInvocationMetric;
import ru.tfs.autoconfigure.metrics.MetricStatService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    private final MetricStatService metricStatService;

    Map<String, Class<?>> map = new HashMap<>();

    public CustomBeanPostProcessor(MetricStatService metricStatService) {
        this.metricStatService = metricStatService;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (hasAnyTimedMethod(bean)) {
            map.put(beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = map.get(beanName);
        if (beanClass != null) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) -> {
             if (bean.getClass().isAnnotationPresent(Timed.class) || method.isAnnotationPresent(Timed.class)) {
                        String name = method.getName();
                        LocalDateTime invocationTime = LocalDateTime.now();
                        try {
                            return methodProxy.invokeSuper(o, args);
                        } finally {
                            Integer totalTime = Math.toIntExact(Duration.between(invocationTime, LocalDateTime.now()).toMillis());
                            metricStatService.addStat(new MethodInvocationMetric(name, invocationTime, totalTime));
                        }
                    } else {
                        return methodProxy.invokeSuper(o, args);
                    }
                }
            );
            return enhancer.create();
        }
        return bean;
    }


    private boolean hasAnyTimedMethod(Object bean) {
        return Stream
                .of(ReflectionUtils.getAllDeclaredMethods(bean.getClass()))
                .anyMatch(method -> AnnotationUtils.getAnnotation(method, Timed.class) != null || AnnotationUtils.getAnnotation(bean.getClass(), Timed.class) != null);
    }
}
