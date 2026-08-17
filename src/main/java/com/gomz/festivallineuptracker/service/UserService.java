package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.LoginRequestDTO;
import com.gomz.festivallineuptracker.dto.LoginResponseDTO;
import com.gomz.festivallineuptracker.dto.UserRegistrationRequestDTO;
import com.gomz.festivallineuptracker.dto.UserResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.InvalidCredentialsException;
import com.gomz.festivallineuptracker.model.Role;
import com.gomz.festivallineuptracker.model.User;
import com.gomz.festivallineuptracker.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }








    public UserResponseDTO registerUser(UserRegistrationRequestDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole(), savedUser.getCreatedAt(),
                savedUser.getUpdatedAt());
    }






    public LoginResponseDTO loginUser(LoginRequestDTO dto) {

        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        LoginResponseDTO response = new LoginResponseDTO();

        response.setToken(null);
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());

        return response;
    }





}
