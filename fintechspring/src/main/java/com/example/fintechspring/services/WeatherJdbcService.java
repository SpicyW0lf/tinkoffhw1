package com.example.fintechspring.services;

import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.DTO.WeatherDataDTO.WeatherRequest;
import com.example.fintechspring.models.JDBC.CityJ;
import com.example.fintechspring.models.JDBC.WeatherJ;
import com.example.fintechspring.models.JDBC.WeatherTypeJ;
import com.example.fintechspring.repositories.JDBC.CityJDBCRepo;
import com.example.fintechspring.repositories.JDBC.WeatherJDBCRepo;
import com.example.fintechspring.repositories.JDBC.WeatherTypeJDBCRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class WeatherJdbcService {
    private final WeatherTypeJDBCRepo weatherTypeJDBCRepo;
    private final WeatherJDBCRepo weatherJDBCRepo;
    private final CityJDBCRepo cityJDBCRepo;

    public List<WeatherTypeJ> findAllTypes() {
        return weatherTypeJDBCRepo.findAll();
    }

    public void createType(String type) {
        weatherTypeJDBCRepo.save(type);
    }

    public void updateType(String oldType, String newType) {
        weatherTypeJDBCRepo.update(oldType, newType);
    }

    public List<WeatherJ> findAllWeather() {
        return weatherJDBCRepo.findAll();
    }

    public void createWeather(WeatherRequest wr) {
        WeatherTypeJ type = weatherTypeJDBCRepo.findByName(wr.getType()).orElseThrow(NoSuchElementException::new);
        weatherJDBCRepo.save(new WeatherJ(wr.getTemperature(), type.getId()));
    }

    public void deleteType(String type) {
        weatherTypeJDBCRepo.deleteType(type);
    }

    public List<CityJ> findAllCities() {
        return cityJDBCRepo.findAll();
    }

    public void createCity(CityRequest cr) {
        WeatherTypeJ type = weatherTypeJDBCRepo.findByName(cr.getWeather().getType()).orElseThrow(NoSuchElementException::new);
        WeatherJ weather = weatherJDBCRepo.findByTemperatureAndType(cr.getWeather().getTemperature(), type.getId()).orElseThrow(NoSuchElementException::new);
        cityJDBCRepo.save(new CityJ(
                cr.getName(),
                cr.getDate(),
                weather.getId()
        ));
    }

    public void deleteCity(CityRequest cr) {
        cityJDBCRepo.deleteByNameAndDate(cr);
    }
}
