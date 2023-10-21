package com.example.fintechspring.controllers;

import com.example.fintechspring.DTO.ResponseDTO;
import com.example.fintechspring.DTO.WeatherDTO;
import com.example.fintechspring.exceptions.BadArgumentsException;
import com.example.fintechspring.exceptions.NoArgumentException;
import com.example.fintechspring.models.Weather;
import com.example.fintechspring.services.WeatherApiService;
import com.example.fintechspring.services.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/wheather")
@AllArgsConstructor
@Tag(name = "Погода", description = "Операции над погодой")
public class WeatherController {
    private final List<Weather> weathers = new ArrayList<>();
    private final WeatherService service;
    private final WeatherApiService weatherApiService;

    @GetMapping("/{city}")
    @Operation(summary = "Получить погоду", description = "Возвращает все температуры за текущую дату")
    public ResponseEntity<Map<LocalTime, List<Integer>>> getWeather(@Parameter(description = "Название города") @PathVariable String city) throws JsonProcessingException, NoArgumentException {
        if (city == null) {
            throw new NoArgumentException();
        }

        return ResponseEntity.ok(service.findTemperaturesForToday(weathers, city));
    }

    @GetMapping("/weather")
    @Operation(summary = "Получить погоду с Api", description = "Получить погоду по городу с внешнего сервиса")
    public ResponseEntity<WeatherDTO> check(@RequestParam String city) {
        return ResponseEntity.ok(weatherApiService.getWeatherByCity(city));
    }

    @PostMapping("/{city}")
    @Operation(summary = "Добавить погоду", description = "Добавляет новые данные о погоде")
    public ResponseEntity<ResponseDTO> postWeather(@RequestBody WeatherDTO weather, @Parameter(description = "Название города") @PathVariable String city) throws NoArgumentException, BadArgumentsException {
        weather.setName(city);
        if (weather.getDate() == null || weather.getName() == null || weather.getTemperature() == null) {
            throw new NoArgumentException();
        }

        if (!service.createWeather(weathers, weather)) {
            throw new BadArgumentsException("This wheather is already exists, if you want to change it use PUT method");
        }

        return ResponseEntity.ok(new ResponseDTO("Wheather was added successfully!"));
    }

    @PutMapping("/{city}")
    @Operation(summary = "Изменить погоду", description = "Изменить данные о погоде за конкретное время")
    public ResponseEntity<ResponseDTO> putWeather(@RequestBody WeatherDTO weather, @Parameter(description = "Название города") @PathVariable String city) throws NoArgumentException, BadArgumentsException {
        weather.setName(city);
        if (weather.getDate() == null || weather.getName() == null || weather.getTemperature() == null) {
            throw new NoArgumentException();
        }

        if (service.updateWeather(weathers, weather)) {
            return ResponseEntity.ok(new ResponseDTO("Weather updated successfully!"));
        }

        return ResponseEntity.ok(new ResponseDTO("Created new weather"));
    }

    @DeleteMapping("/{city}")
    @Operation(summary = "Удалить погоду", description = "Удаляет все данные о погоде у конкретного города")
    public ResponseEntity<ResponseDTO> deleteWeather(@Parameter(description = "Название города") @PathVariable String city) throws NoArgumentException, BadArgumentsException {
        if (city == null) {
            throw new NoArgumentException();
        }

        if (service.deleteWeather(weathers, city)) {
            return ResponseEntity.ok(new ResponseDTO("Successfully deleted some elements"));
        }

        throw new BadArgumentsException("Cant find any cities");
    }
}