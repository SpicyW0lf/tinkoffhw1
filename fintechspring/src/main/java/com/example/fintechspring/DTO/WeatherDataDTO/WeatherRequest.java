package com.example.fintechspring.DTO.WeatherDataDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherRequest {
    private int temperature;
    private String type;
}

