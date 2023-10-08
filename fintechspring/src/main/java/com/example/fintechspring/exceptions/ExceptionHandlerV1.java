package com.example.fintechspring.exceptions;

import com.example.fintechspring.DTO.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.example.fintechspring"})
public class ExceptionHandlerV1 {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> serverError() {
        return ResponseEntity.internalServerError().body(new ResponseDTO("Internal server error"));
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
}
