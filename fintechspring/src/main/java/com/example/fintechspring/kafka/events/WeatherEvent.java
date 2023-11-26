package com.example.fintechspring.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class WeatherEvent {
    private String key;
    private String name;
    private LocalDateTime time;
    private int temperature;
    private String type;

    public WeatherEvent(
            String key,
            String name,
            LocalDateTime time,
            int temperature,
            String type
    ) {
        this.key = key;
        this.temperature = temperature;
        this.name = name;
        this.time = time;
        this.type = type;
    }
}
