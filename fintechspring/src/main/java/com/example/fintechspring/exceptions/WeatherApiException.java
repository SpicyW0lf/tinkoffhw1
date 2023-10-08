package com.example.fintechspring.exceptions;

import com.example.fintechspring.exceptions.exceptionResponse.ErrorResponse;

import java.io.IOException;

public class WeatherApiException extends IOException {
    private final ErrorResponse errorResponse;

    public WeatherApiException(ErrorResponse er) {
        this.errorResponse = er;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
