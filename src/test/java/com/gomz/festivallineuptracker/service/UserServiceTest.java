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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_success_doesNotExposePassword() {
        when(userRepository.existsByUsername("om")).thenReturn(false);
        when(userRepository.existsByEmail("om@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password1")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            ReflectionTestUtils.setField(user, "id", 1);
            ReflectionTestUtils.setField(user, "createdAt", LocalDateTime.now());
            ReflectionTestUtils.setField(user, "updatedAt", LocalDateTime.now());
            return user;
        });

        UserResponseDTO result = userService.registerUser(registration("om", "om@test.com", "password1"));

        assertEquals("om", result.getUsername());
        assertEquals("om@test.com", result.getEmail());
        assertEquals(Role.USER, result.getRole());
    }

    @Test
    void registerUser_duplicateUsername_throwsConflict() {
        when(userRepository.existsByUsername("om")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> userService.registerUser(registration("om", "om@test.com", "password1")));
    }

    @Test
    void registerUser_duplicateEmail_throwsConflict() {
        when(userRepository.existsByUsername("om")).thenReturn(false);
        when(userRepository.existsByEmail("om@test.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> userService.registerUser(registration("om", "om@test.com", "password1")));
    }

    @Test
    void loginUser_success_returnsTokenWithoutPassword() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1);
        user.setUsername("om");
        user.setPassword("hashed");
        user.setRole(Role.USER);
        when(userRepository.findByUsername("om")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password1", "hashed")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("om");
        request.setPassword("password1");

        LoginResponseDTO result = userService.loginUser(request);

        assertEquals("jwt-token", result.getToken());
        assertEquals("om", result.getUsername());
        assertEquals(Role.USER, result.getRole());
    }

    @Test
    void loginUser_unknownUsername_throwsUnauthorized() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("missing");
        request.setPassword("password1");

        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(request));
    }

    @Test
    void loginUser_wrongPassword_throwsUnauthorized() {
        User user = new User();
        user.setUsername("om");
        user.setPassword("hashed");
        when(userRepository.findByUsername("om")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("bad-password", "hashed")).thenReturn(false);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("om");
        request.setPassword("bad-password");

        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(request));
    }

    private UserRegistrationRequestDTO registration(String username, String email, String password) {
        UserRegistrationRequestDTO dto = new UserRegistrationRequestDTO();
        dto.setUsername(username);
        dto.setEmail(email);
        dto.setPassword(password);
        return dto;
    }
}
