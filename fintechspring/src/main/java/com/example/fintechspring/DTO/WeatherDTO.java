package com.example.fintechspring.DTO;

import com.example.fintechspring.models.Weather;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@ToString
@Schema(description = "Сущность погоды")
public class WeatherDTO {
    @Schema(hidden = true)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String name;
    @Schema(description = "Температура")
    private Integer temperature;
    @Schema(description = "Дата и время", example = "2023-10-01 12:12", type = "string")
    private LocalDateTime date;
    @Schema(description = "Тип погоды", example = "Sunny", type = "string")
    private String type;

    public WeatherDTO(Integer temperature, String date) {
        this.temperature = temperature;
        this.date = LocalDateTime.parse(date, DATE_FORMAT);
    }

    public Weather toWeather() {
        return Weather.of(name, temperature, date);
    }
}
