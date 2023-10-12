package com.example.fintechspring.services;

import com.example.fintechspring.DTO.WeatherApiDTO.ApiResponse;
import com.example.fintechspring.DTO.WeatherDTO;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherApiService {
    @Qualifier("template")
    private final RestTemplate rest;
    @Value("${weather-api.url}")
    private String url;
    @Value("${weather-api.token}")
    private String token;
    private final RateLimiter rateLimiter;

    public WeatherApiService(RestTemplate rest, RateLimiter rl) {
        this.rest = rest;
        this.rateLimiter = rl;
    }

    public WeatherDTO getWeatherByCity(String city) {
        ApiResponse response = RateLimiter.decorateFunction(rateLimiter, this::getWeather).apply(city);
        WeatherDTO weatherDTO = new WeatherDTO(response.getCurrent().getTemp().intValue(), response.getCurrent().getDate());
        weatherDTO.setName(response.getLocation().getName());
        return weatherDTO;
    }

    private ApiResponse getWeather(String city) {
        ApiResponse response = rest.getForObject(
                url + "?key=" + token + "&q=" + city,
                ApiResponse.class
        );
        return response;
    }
}
