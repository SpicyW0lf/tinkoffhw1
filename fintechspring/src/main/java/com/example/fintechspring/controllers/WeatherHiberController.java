package com.example.fintechspring.controllers;

import com.example.fintechspring.DTO.ResponseDTO;
import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.DTO.WeatherDataDTO.WeatherRequest;
import com.example.fintechspring.models.City;
import com.example.fintechspring.models.WeatherEntity;
import com.example.fintechspring.models.WeatherType;
import com.example.fintechspring.services.CityHiberService;
import com.example.fintechspring.services.WeatherHiberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wheather/hiber")
@AllArgsConstructor
public class WeatherHiberController {
    private final WeatherHiberService weatherService;
    private final CityHiberService cityService;

    @GetMapping("/allTypes")
    public List<WeatherType> getAllTypes() {
        return weatherService.getAllWeatherTypes();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ResponseDTO> deleteType(@PathVariable String type) {
        weatherService.deleteType(type);

        return ResponseEntity.ok(new ResponseDTO("Successfully deleted weather type"));
    }

    @PostMapping("/type/{type}")
    public ResponseEntity<ResponseDTO> createType(@PathVariable String type) {
        weatherService.createType(type);

        return ResponseEntity.ok(new ResponseDTO("Successfully created new type"));
    }

    @PutMapping("/type/{oldType}")
    public ResponseEntity<ResponseDTO> updateType(@PathVariable String oldType, @RequestParam String type) {
        weatherService.updateType(oldType, type);

        return ResponseEntity.ok(new ResponseDTO("Successfully updated type"));
    }

    @GetMapping("/weather/all")
    public List<WeatherEntity> findAllWeather() {
        return weatherService.getAllWeather();
    }

    @PostMapping("/weather/new")
    public ResponseEntity<ResponseDTO> createWeather(@RequestBody WeatherRequest weather) {
        weatherService.createWeather(weather.getTemperature(), weather.getType());

        return ResponseEntity.ok(new ResponseDTO("Successfully created new weather"));
    }

    @GetMapping("/city/{city}")
    public List<City> findCities(@PathVariable String city) {
        return cityService.getCities(city);
    }

    @PostMapping("/city/new")
    public ResponseEntity<ResponseDTO> createCity(@RequestBody CityRequest city) {
        cityService.createCity(city);

        return ResponseEntity.ok(new ResponseDTO("Created new city"));
    }

    @DeleteMapping("/city/delete")
    public ResponseEntity<ResponseDTO> deleteCity(@RequestBody CityRequest city) {
        cityService.deleteCity(city);

        return ResponseEntity.ok(new ResponseDTO("The city was deleted"));
    }

    @PutMapping("/city/update")
    public ResponseEntity<ResponseDTO> updateWeatherInCity(@RequestBody CityRequest city) {
        cityService.updateWeatherInCity(city);

        return ResponseEntity.ok(new ResponseDTO("The city was updated"));
    }
}
