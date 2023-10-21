package com.example.fintechspring.controllers;

import com.example.fintechspring.DTO.ResponseDTO;
import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.DTO.WeatherDataDTO.WeatherRequest;
import com.example.fintechspring.models.JDBC.CityJ;
import com.example.fintechspring.models.JDBC.WeatherJ;
import com.example.fintechspring.models.JDBC.WeatherTypeJ;
import com.example.fintechspring.services.WeatherJdbcService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/wheather/jdbc")
public class WeatherJDBCController {
    private final WeatherJdbcService jdbcService;

    @GetMapping("/type/all")
    public List<WeatherTypeJ> findAllTypes() {
        return jdbcService.findAllTypes();
    }

    @PostMapping("/type/new/{type}")
    public ResponseEntity<ResponseDTO> createType(@PathVariable String type) {
        jdbcService.createType(type);

        return ResponseEntity.ok(new ResponseDTO("Type created"));
    }

    @DeleteMapping("/type/delete/{type}")
    public ResponseEntity<ResponseDTO> deleteType(@PathVariable String type) {
        jdbcService.deleteType(type);

        return ResponseEntity.ok(new ResponseDTO("Type deleted"));
    }

    @PutMapping("/type/update/{oldType}")
    public ResponseEntity<ResponseDTO> updateType(@PathVariable String oldType, @RequestParam String type) {
        jdbcService.updateType(oldType, type);

        return ResponseEntity.ok(new ResponseDTO("Type updated"));
    }

    @GetMapping("/weather/all")
    public List<WeatherJ> findAllWeather() {
        return jdbcService.findAllWeather();
    }

    @PostMapping("/weather/new")
    public ResponseEntity<ResponseDTO> createWeather(@RequestBody WeatherRequest wr) {
        jdbcService.createWeather(wr);

        return ResponseEntity.ok(new ResponseDTO("Weather created"));
    }

    @GetMapping("/city/all")
    public List<CityJ> findAllCities() {
        return jdbcService.findAllCities();
    }

    @PostMapping("/city/new")
    public ResponseEntity<ResponseDTO> createCity(@RequestBody CityRequest cr) {
        jdbcService.createCity(cr);

        return ResponseEntity.ok(new ResponseDTO("City created"));
    }

    @DeleteMapping("/city/delete")
    public ResponseEntity<ResponseDTO> deleteCity(@RequestBody CityRequest cr) {
        jdbcService.deleteCity(cr);

        return ResponseEntity.ok(new ResponseDTO("City deleted"));
    }
}
