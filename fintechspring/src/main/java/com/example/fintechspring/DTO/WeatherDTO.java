package com.example.fintechspring.DTO;

import com.example.fintechspring.models.Weather;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@ToString
@Schema(description = "Сущность погоды")
public class WeatherDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String name;
    @Schema(description = "Температура")
    private Integer temperature;
    @Schema(description = "Дата и время", example = "2023-10-01 12:12:12", type = "string")
    private LocalDateTime date;

    public WeatherDTO(Integer temperature, String date) {
        this.temperature = temperature;
        this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Weather toWeather() {
        return Weather.of(name, temperature, date);
    }
}
