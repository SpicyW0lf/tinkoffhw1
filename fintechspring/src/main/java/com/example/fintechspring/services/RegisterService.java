package com.example.fintechspring.services;

import com.example.fintechspring.DTO.RegisterDTO;
import com.example.fintechspring.exceptions.BadArgumentsException;
import com.example.fintechspring.exceptions.UserAlreadyExistsException;
import com.example.fintechspring.models.Role;
import com.example.fintechspring.models.User;
import com.example.fintechspring.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterDTO registerDTO) throws UserAlreadyExistsException, BadArgumentsException {
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        if (!isPasswordCorrect(registerDTO.getPassword())) {
            throw new BadArgumentsException("Bad password");
        }

        userRepository.save(new User(
                registerDTO.getUsername(),
                passwordEncoder.encode(registerDTO.getPassword())
        ));
    }

    private boolean isPasswordCorrect(String password) {
        return password != null && password.length() >= 4;
    }
}
