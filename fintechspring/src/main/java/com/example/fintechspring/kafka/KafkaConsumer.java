package com.example.fintechspring.kafka;

import com.example.fintechspring.DTO.WeatherDataDTO.CityRequest;
import com.example.fintechspring.DTO.WeatherDataDTO.WeatherRequest;
import com.example.fintechspring.exceptions.RuntimeSQLException;
import com.example.fintechspring.kafka.events.WeatherEvent;
import com.example.fintechspring.models.JDBC.CityJ;
import com.example.fintechspring.services.WeatherJdbcService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class KafkaConsumer {
    private final WeatherJdbcService jdbcService;

    @KafkaListener(topics = "WeatherTopic", groupId = "com.example")
    public void weatherEventConsumer(WeatherEvent event) throws SQLException {
        jdbcService.createCity(new CityRequest(
                event.getName(),
                event.getTime(),
                new WeatherRequest(
                        event.getTemperature(),
                        event.getType()
                )
        ));
        List<CityJ> cities = jdbcService.findAllCitiesByName(event.getName());
        if (cities.size() > 30) {
            CityJ removed = cities.remove(cities.size() - 2);
            jdbcService.deleteCityById(removed.getId());
        }

        System.out.println(event.getName() + " average temperature: " + cities.stream()
                .map(city -> {
                    try {
                        return jdbcService.findWeatherById(city.getWeatherId()).getTemperature();
                    } catch (SQLException e) {
                        throw new RuntimeSQLException();
                    }
                })
                .reduce(0, Integer::sum) / cities.size());
    }
}
