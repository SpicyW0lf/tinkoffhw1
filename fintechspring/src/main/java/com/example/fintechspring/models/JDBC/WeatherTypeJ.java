package com.example.fintechspring.models.JDBC;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherTypeJ {
    private int id;
    private String type;

    public WeatherTypeJ(String type) {
        this.type = type;
    }
}
