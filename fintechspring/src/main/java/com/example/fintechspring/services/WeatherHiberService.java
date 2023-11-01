package com.example.fintechspring.services;

import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.models.City;
import com.example.fintechspring.models.WeatherEntity;
import com.example.fintechspring.models.WeatherType;
import com.example.fintechspring.repositories.CityHiberRepository;
import com.example.fintechspring.repositories.WeatherHiberRepository;
import com.example.fintechspring.repositories.WeatherTypeHiberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class WeatherHiberService {
    private final CityHiberRepository cityRepositoty;
    private final WeatherHiberRepository weatherRepository;
    private final WeatherTypeHiberRepository typeRepository;

    public List<WeatherType> getAllWeatherTypes() {
        return typeRepository.findAll();
    }

    public void deleteType(String type) {
        WeatherType type1 = typeRepository.findByName(type).orElseThrow(NoSuchElementException::new);
        typeRepository.delete(type1);
    }

    public WeatherType createType(String type) {
        return typeRepository.save(new WeatherType(type));
    }

    public void updateType(String oldType, String newType) throws NoSuchElementException {
        WeatherType type = typeRepository.findByName(oldType).orElseThrow(NoSuchElementException::new);
        type.setName(newType);
        typeRepository.flush();
    }

    public List<WeatherEntity> getAllWeather() {
        return weatherRepository.findAll();
    }

    public WeatherEntity createWeather(int temperature, String type) throws NoSuchElementException {
        WeatherType wt = typeRepository.findByName(type).orElseThrow(NoSuchElementException::new);
        WeatherEntity we = new WeatherEntity(temperature, wt);
        wt.addWeather(we);
        return weatherRepository.save(we);
    }
}
