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

@AllArgsConstructor
@Service
public class CityHiberService {
    private final WeatherTypeHiberRepository typeRepository;
    private final CityHiberRepository cityRepository;
    private final WeatherHiberRepository weatherRepository;
    private final WeatherHiberService weatherService;
    
    public void updateWeatherInCity(CityRequest cityRequest) {
        City city = cityRepository.findByNameAndDate(cityRequest.getName(), cityRequest.getDate()).orElseThrow(NoSuchElementException::new);
        WeatherEntity oldWeather = city.getWeather();
        oldWeather.deleteCity(city);
        WeatherType type = typeRepository.findByName(cityRequest.getWeather().getType()).orElseThrow(NoSuchElementException::new);
        WeatherEntity newWeather = weatherRepository.findByTypeAndTemperature(type, cityRequest.getWeather().getTemperature())
                .orElseGet(() -> weatherRepository.save(new WeatherEntity(cityRequest.getWeather().getTemperature(), type)));
        newWeather.addCity(city);
    }

    public List<City> getCities(String name) {
        return cityRepository.findAllByName(name);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createCity(CityRequest cityRequest) {
        WeatherType type = typeRepository.findByName(cityRequest.getWeather().getType())
                .orElseGet(() -> weatherService.createType(cityRequest.getWeather().getType()));
        WeatherEntity weather = weatherRepository.findByTypeAndTemperature(type, cityRequest.getWeather().getTemperature())
                .orElseGet(() -> weatherService.createWeather(cityRequest.getWeather().getTemperature(), type.getName()));
        City city = new City(cityRequest.getName(), cityRequest.getDate(), weather);
        weather.addCity(city);
        cityRepository.save(city);
    }

    public void deleteCity(CityRequest cityRequest) {
        City city = cityRepository.findByNameAndDate(cityRequest.getName(), cityRequest.getDate()).orElseThrow(NoSuchElementException::new);
        city.getWeather().deleteCity(city);
        cityRepository.delete(city);
    }
}
