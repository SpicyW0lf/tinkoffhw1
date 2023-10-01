package com.example.fintechspring.controllers;

import com.example.fintechspring.DTO.WeatherDTO;
import com.example.fintechspring.exceptions.NullException;
import com.example.fintechspring.models.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wheather")
@AllArgsConstructor
@Tag(name = "Погода", description = "Операции над погодой")
public class WeatherController {
    private final List<Weather> weathers = new ArrayList<>();
    private final ObjectMapper mapper;

    @GetMapping("/{city}")
    @Operation(summary = "Получить погоду", description = "Возвращает все температуры за текущую дату")
    public ResponseEntity<String> getWeather(@Parameter(description = "Название города") @PathVariable String city) throws JsonProcessingException, NullException {
        LocalDate date = LocalDate.now();
        if (city == null) {
            throw new NullException();
        }

        return ResponseEntity.ok(
                mapper.writeValueAsString(weathers.stream()
                        .filter(it -> it.getName().equals(city) && it.getDate().toLocalDate().equals(date))
                        .collect(Collectors.groupingBy(it -> it.getDate().toLocalTime(),
                                Collectors.mapping(Weather::getTemperature, Collectors.toList())
                                ))
                )
                );
    }

    @PostMapping("/{city}")
    @Operation(summary = "Добавить погоду", description = "Добавляет новые данные о погоде")
    public ResponseEntity<Map<String, String>> postWeather(@RequestBody WeatherDTO weather, @Parameter(description = "Название города") @PathVariable String city) throws NullException {
        weather.setName(city);
        if (weather.getDate() == null || weather.getName() == null || weather.getTemperature() == null) {
            throw new NullException();
        }

        for (Weather exists : weathers) {
            if (exists.getName().equals(weather.getName()) && exists.getDate().equals(weather.getDate())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message","This wheather is already exists, if you want to change it use PUT method"));
            }
        }

        weathers.add(weather.toWeather());

        return ResponseEntity.ok(Map.of("message","Wheather was added successfully!"));
    }

    @PutMapping("/{city}")
    @Operation(summary = "Изменить погоду", description = "Изменить данные о погоде за конкретное время")
    public ResponseEntity<Map<String,String>> putWeather(@RequestBody WeatherDTO weather, @Parameter(description = "Название города") @PathVariable String city) throws NullException {
        weather.setName(city);
        if (weather.getDate() == null || weather.getName() == null || weather.getTemperature() == null) {
            throw new NullException();
        }

        for (Weather exists : weathers) {
            if (exists.getName().equals(weather.getName()) && exists.getDate().equals(weather.getDate())) {
                if (exists.getTemperature().equals(weather.getTemperature())) {
                    return ResponseEntity.badRequest().body(Map.of("message","You cant update temperature, its the same!"));
                }

                exists.setTemperature(weather.getTemperature());
                return ResponseEntity.ok(Map.of("message","Weather updated successfully!"));
            }
        }

        return ResponseEntity.badRequest().body(Map.of("message","Cant find info, wrong city or time!"));
    }

    @DeleteMapping("/{city}")
    @Operation(summary = "Удалить погоду", description = "Удаляет все данные о погоде у конкретного города")
    public ResponseEntity<Map<String, String>> deleteWeather(@Parameter(description = "Название города") @PathVariable String city) throws NullException {
        if (city == null) {
            throw new NullException();
        }
        boolean deleted = weathers.removeIf(it -> it.getName().equals(city));

        if (deleted) {
            return ResponseEntity.ok(Map.of("message","Successfully deleted some elements"));
        }
        return ResponseEntity.badRequest().body(Map.of("message","Cant find any cities"));
    }

    @ExceptionHandler(NullException.class)
    public ResponseEntity<Map<String, String>> nullError() {
        return ResponseEntity.badRequest().body(Map.of("message", "Incorrect input!"));
    }
}
