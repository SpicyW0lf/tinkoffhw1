package com.example.fintechspring.services;

import com.example.fintechspring.DTO.WeatherDTO;
import com.example.fintechspring.models.Weather;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    public Map<LocalTime, List<Integer>> findTemperaturesForToday(List<Weather> weathers, String city) {
        LocalDate date = LocalDate.now();

        return weathers.stream()
                .filter(it -> it.getName().equals(city) && it.getDate().toLocalDate().equals(date))
                .collect(Collectors.groupingBy((Weather it) -> it.getDate().toLocalTime(),
                        Collectors.mapping(Weather::getTemperature, Collectors.toList())
                ));
    }

    public boolean createWeather(List<Weather> weathers, WeatherDTO weather) {
        for (Weather exists : weathers) {
            if (exists.getName().equals(weather.getName()) && exists.getDate().equals(weather.getDate())) {
                return false;
            }
        }
        return weathers.add(weather.toWeather());
    }

    public boolean updateWeather(List<Weather> weathers, WeatherDTO weather) {
        for (Weather exists : weathers) {
            if (exists.getName().equals(weather.getName()) && exists.getDate().equals(weather.getDate())) {
                exists.setTemperature(weather.getTemperature());
                return true;
            }
        }

        weathers.add(weather.toWeather());
        return false;
    }

    public boolean deleteWeather(List<Weather> weathers, String city) {
        return weathers.removeIf(it -> it.getName().equals(city));
    }
}
