package com.example.fintechspring.controllers;

import com.example.fintechspring.DTO.RegisterDTO;
import com.example.fintechspring.DTO.ResponseDTO;
import com.example.fintechspring.exceptions.BadArgumentsException;
import com.example.fintechspring.exceptions.UserAlreadyExistsException;
import com.example.fintechspring.models.User;
import com.example.fintechspring.services.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegisterDTO registerDTO) throws BadArgumentsException, UserAlreadyExistsException {
        registerService.registerUser(registerDTO);

        return ResponseEntity.ok(new ResponseDTO("User successfully created"));
    }
}
