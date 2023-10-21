package com.example.fintechspring.models.JDBC;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CityJ {
    private int id;
    private String name;
    private LocalDateTime date;
    private int weatherId;

    public CityJ(String name, LocalDateTime date, int weatherId) {
        this.name = name;
        this.date = date;
        this.weatherId = weatherId;
    }
}
