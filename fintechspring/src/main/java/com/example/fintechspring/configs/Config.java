package com.example.fintechspring.configs;

import com.example.fintechspring.exceptions.RestTemplateResponseErrorHandler;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class Config {
    @Bean("template")
    public RestTemplate restTemplate(RestTemplateBuilder builder, RestTemplateResponseErrorHandler re) {
        return builder
                .errorHandler(re)
                .build();
    }

    @Bean
    public RateLimiter rateLimitFunction(@Value("${weather-api.limit}") int limit) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofDays(30))
                .limitForPeriod(limit)
                .build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);

        return registry.rateLimiter("limiter");
    }
}
