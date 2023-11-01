package com.example.fintechspring.models.JDBC;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherJ {
    private int id;
    private int temperature;
    private int typeId;

    public WeatherJ(int temperature, int type) {
        this.temperature = temperature;
        this.typeId = type;
    }
}
