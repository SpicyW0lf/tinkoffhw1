package com.example.fintechspring.exceptions.exceptionResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeatherException {
    private int code;
    private String message;
}
