package com.example.fintechspring.exceptions;

import com.example.fintechspring.exceptions.exceptionResponse.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@AllArgsConstructor
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new WeatherApiException(objectMapper.readValue(response.getBody(), ErrorResponse.class));
    }
}
