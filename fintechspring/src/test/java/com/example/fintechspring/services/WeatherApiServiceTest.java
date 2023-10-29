package com.example.fintechspring.services;

import com.example.fintechspring.DTO.WeatherApiDTO.ApiResponse;
import com.example.fintechspring.DTO.WeatherApiDTO.Condition;
import com.example.fintechspring.DTO.WeatherApiDTO.Current;
import com.example.fintechspring.DTO.WeatherApiDTO.Location;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class WeatherApiServiceTest {

    private RateLimiter rateLimiter = RateLimiterRegistry.ofDefaults().rateLimiter("limiter");
    private RestTemplate rest = Mockito.mock(RestTemplate.class);
    private WeatherApiService apiService = new WeatherApiService(rest, rateLimiter);

    @Test
    void getWeatherByCityShouldReturnCity() {
        ApiResponse response = new ApiResponse(
                new Location("Moscow"),
                new Current(15.2, "2022-02-01 12:12", new Condition("Sunny"))
        );
        Mockito.when(rest.getForObject(null + "?key="+ null + "&q=" + "Moscow", ApiResponse.class)).thenReturn(response);
        assertEquals("Moscow", apiService.getWeatherByCity("Moscow").getName());
    }
}