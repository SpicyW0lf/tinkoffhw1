package com.example.fintechspring.controllers;

import com.example.fintechspring.DTO.ResponseDTO;
import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.DTO.WeatherDataDTO.WeatherRequest;
import com.example.fintechspring.cache.WeatherTypeCache;
import com.example.fintechspring.exceptions.RuntimeSQLException;
import com.example.fintechspring.models.JDBC.CityJ;
import com.example.fintechspring.models.JDBC.WeatherJ;
import com.example.fintechspring.models.JDBC.WeatherTypeJ;
import com.example.fintechspring.services.WeatherJdbcService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/wheather/jdbc")
public class WeatherJDBCController {
    private final WeatherJdbcService jdbcService;
    private final WeatherTypeCache cache;

    @GetMapping("/type/all")
    public List<WeatherTypeJ> findAllTypes() throws SQLException {
        return jdbcService.findAllTypes();
    }

    @GetMapping("/type/{type}")
    public WeatherTypeJ findType(@PathVariable String type) {
        return cache.get(type).orElseGet(() -> {
            try {
                WeatherTypeJ weatherTypeJ = jdbcService.findType(type);
                cache.put(weatherTypeJ);
                return weatherTypeJ;
            } catch (SQLException e) {
                throw new RuntimeSQLException();
            }
        });
    }

    @PostMapping("/type/new/{type}")
    public ResponseEntity<ResponseDTO> createType(@PathVariable String type) throws SQLException {
        jdbcService.createType(type);
        cache.put(jdbcService.findType(type));

        return ResponseEntity.ok(new ResponseDTO("Type created"));
    }

    @DeleteMapping("/type/delete/{type}")
    public ResponseEntity<ResponseDTO> deleteType(@PathVariable String type) throws SQLException {
        jdbcService.deleteType(type);
        cache.delete(type);

        return ResponseEntity.ok(new ResponseDTO("Type deleted"));
    }

    @PutMapping("/type/update/{oldType}")
    public ResponseEntity<ResponseDTO> updateType(@PathVariable String oldType, @RequestParam String type) throws SQLException {
        jdbcService.updateType(oldType, type);
        WeatherTypeJ newType = jdbcService.findType(type);
        cache.delete(type);
        cache.put(newType);

        return ResponseEntity.ok(new ResponseDTO("Type updated"));
    }

    @GetMapping("/weather/all")
    public List<WeatherJ> findAllWeather() throws SQLException {
        return jdbcService.findAllWeather();
    }

    @PostMapping("/weather/new")
    public ResponseEntity<ResponseDTO> createWeather(@RequestBody WeatherRequest wr) throws SQLException {
        jdbcService.createWeather(wr);

        return ResponseEntity.ok(new ResponseDTO("Weather created"));
    }

    @GetMapping("/city/all")
    public List<CityJ> findAllCities() throws SQLException {
        return jdbcService.findAllCities();
    }

    @PostMapping("/city/new")
    public ResponseEntity<ResponseDTO> createCity(@RequestBody CityRequest cr) throws SQLException {
        jdbcService.createCity(cr);

        return ResponseEntity.ok(new ResponseDTO("City created"));
    }

    @DeleteMapping("/city/delete")
    public ResponseEntity<ResponseDTO> deleteCity(@RequestBody CityRequest cr) throws SQLException {
        jdbcService.deleteCity(cr);

        return ResponseEntity.ok(new ResponseDTO("City deleted"));
    }
}
