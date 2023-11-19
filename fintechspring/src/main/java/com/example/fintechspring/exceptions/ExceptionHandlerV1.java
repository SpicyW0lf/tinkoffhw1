package com.example.fintechspring.exceptions;

import com.example.fintechspring.DTO.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = {"com.example.fintechspring"})
public class ExceptionHandlerV1 {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> serverError() {
        return ResponseEntity.internalServerError().body(new ResponseDTO("Internal server error"));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseDTO> userError() {
        return ResponseEntity.badRequest().body(new ResponseDTO("User already exists"));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseDTO> noElement() {
        return ResponseEntity.badRequest().body(new ResponseDTO("One of elements not found in DB"));
    }

    @ExceptionHandler(NoArgumentException.class)
    public ResponseEntity<ResponseDTO> noArgumentError() {
        return ResponseEntity.badRequest().body(new ResponseDTO("Incorrect input!"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDTO> noParamsError() {
        return ResponseEntity.badRequest().body(new ResponseDTO("You didnt enter required parameters"));
    }

    @ExceptionHandler(BadArgumentsException.class)
    public ResponseEntity<ResponseDTO> badArgumentsError(BadArgumentsException ex) {
        return ResponseEntity.badRequest().body(new ResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(WeatherApiException.class)
    public ResponseEntity<ResponseDTO> weatherApiException(WeatherApiException ex) {
        ResponseDTO response = new ResponseDTO(ex.getErrorResponse().getError().getMessage());
        switch (ex.getErrorResponse().getError().getCode()) {
            case 1003, 1005, 1006, 9000, 9001, 9999 -> {
                return ResponseEntity.badRequest().body(response);
            }
            case 1002, 2006 -> {
                return ResponseEntity.status(401).body(response);
            }
        }
        return ResponseEntity.status(403).body(response);
    }
}
