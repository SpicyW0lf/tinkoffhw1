package com.example.fintechspring;

import com.example.fintechspring.DTO.WeatherDTO;
import com.example.fintechspring.kafka.KafkaProducer;
import com.example.fintechspring.kafka.events.WeatherEvent;
import com.example.fintechspring.services.WeatherApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@Component
public class ScheduledTask {
    private final Queue<String> queue;
    private final WeatherApiService apiService;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public ScheduledTask(KafkaProducer kafkaProducer, WeatherApiService apiService, @Value("${kafka.cities}") List<String> cities) {
        this.apiService = apiService;
        this.queue = new ArrayDeque<>(cities);
        this.kafkaProducer = kafkaProducer;
    }

    @Scheduled(fixedDelay = 2000, initialDelay = 10000)
    public void schedule() {
        String polled = queue.poll();
        queue.add(polled);
        WeatherDTO weather = apiService.getWeatherByCity(polled);
        kafkaProducer.sendWeatherEvent(new WeatherEvent(
                weather.getName(),
                weather.getName(),
                LocalDateTime.now(),
                weather.getTemperature(),
                weather.getType()
        ));
    }
}
