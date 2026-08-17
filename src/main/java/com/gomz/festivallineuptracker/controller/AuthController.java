package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.dto.LoginRequestDTO;
import com.gomz.festivallineuptracker.dto.LoginResponseDTO;
import com.gomz.festivallineuptracker.dto.UserRegistrationRequestDTO;
import com.gomz.festivallineuptracker.dto.UserResponseDTO;
import com.gomz.festivallineuptracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegistrationRequestDTO dto) {

        UserResponseDTO user = userService.registerUser(dto);

        return ResponseEntity.status(201).body(user);

    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO dto) {

        LoginResponseDTO user = userService.loginUser(dto);

        return ResponseEntity.ok(user);
    }
}
