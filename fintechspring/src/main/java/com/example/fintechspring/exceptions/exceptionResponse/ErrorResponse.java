package com.example.fintechspring.exceptions.exceptionResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {
    private WeatherException error;
}
