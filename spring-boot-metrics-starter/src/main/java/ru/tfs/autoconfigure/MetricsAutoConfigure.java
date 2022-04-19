package ru.tfs.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ConditionalOnProperty(name = "metrics.enabled", havingValue = "true")
@EnableConfigurationProperties(MetricsProperties.class)
@ComponentScan
@EnableAsync(proxyTargetClass = true)
public class MetricsAutoConfigure {
}
