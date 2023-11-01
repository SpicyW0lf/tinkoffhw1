package com.example.fintechspring.DTO.WeatherDataDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CityRequest {
    private String name;
    private LocalDateTime date;
    private WeatherRequest weather;
}
