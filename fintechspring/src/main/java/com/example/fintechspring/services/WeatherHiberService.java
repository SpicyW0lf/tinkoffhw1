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

    public void createType(String type) {
        typeRepository.save(new WeatherType(type));
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

    public void updateWeatherInCity(CityRequest city) {
        City city1 = cityRepositoty.findByNameAndDate(city.getName(), city.getDate()).orElseThrow(NoSuchElementException::new);
        WeatherEntity oldWeather = city1.getWeather();
        oldWeather.deleteCity(city1);
        WeatherType type = typeRepository.findByName(city.getWeather().getType()).orElseThrow(NoSuchElementException::new);
        WeatherEntity newWeather = weatherRepository.findByTypeAndTemperature(type, city.getWeather().getTemperature())
                .orElseGet(() -> weatherRepository.save(new WeatherEntity(city.getWeather().getTemperature(), type)));
        newWeather.addCity(city1);
    }

    public List<City> getCities(String name) {
        return cityRepositoty.findAllByName(name);
    }

    public void createCity(CityRequest city) {
        WeatherType type = typeRepository.findByName(city.getWeather().getType()).orElseThrow(NoSuchElementException::new);
        WeatherEntity weather1 = weatherRepository.findByTypeAndTemperature(type, city.getWeather().getTemperature())
                .orElseGet(() -> createWeather(city.getWeather().getTemperature(), type.getName()));
        City city1 = new City(city.getName(), city.getDate(), weather1);
        weather1.addCity(city1);
        cityRepositoty.save(city1);
    }

    public void deleteCity(CityRequest city) {
        City cityc = cityRepositoty.findByNameAndDate(city.getName(), city.getDate()).orElseThrow(NoSuchElementException::new);
        cityc.getWeather().deleteCity(cityc);
        cityRepositoty.delete(cityc);
    }
}
