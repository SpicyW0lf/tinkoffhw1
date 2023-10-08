package com.example.fintechspring.services;

import com.example.fintechspring.DTO.WeatherApiDTO.ApiResponse;
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

    public WeatherApiService(RestTemplate rest) {
        this.rest = rest;
    }

    public ApiResponse getWeatherByCity(String city) {
        ApiResponse response = rest.getForObject(
                url + "?key=" + token + "&q=" + city,
                ApiResponse.class
        );
        return response;
    }
}
