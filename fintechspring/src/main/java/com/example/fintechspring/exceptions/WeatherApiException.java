package com.example.fintechspring.exceptions;

import com.example.fintechspring.exceptions.exceptionResponse.ErrorResponse;


public class WeatherApiException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public WeatherApiException(ErrorResponse er) {
        this.errorResponse = er;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
