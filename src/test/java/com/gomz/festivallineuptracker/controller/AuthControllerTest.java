package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.config.SecurityConfig;
import com.gomz.festivallineuptracker.dto.LoginResponseDTO;
import com.gomz.festivallineuptracker.dto.UserResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.InvalidCredentialsException;
import com.gomz.festivallineuptracker.model.Role;
import com.gomz.festivallineuptracker.security.JwtAuthenticationFilter;
import com.gomz.festivallineuptracker.service.JwtService;
import com.gomz.festivallineuptracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void register_success_returns201WithoutPassword() throws Exception {
        when(userService.registerUser(any())).thenReturn(new UserResponseDTO(
                1, "omuser", "om@test.com", Role.USER, LocalDateTime.now(), LocalDateTime.now()
        ));

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "omuser",
                          "email": "om@test.com",
                          "password": "password1"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("omuser"))
                .andExpect(jsonPath("$.email").value("om@test.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void register_duplicateUsername_returns409() throws Exception {
        when(userService.registerUser(any())).thenThrow(new DuplicateResourceException("Username already exists"));

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "omuser",
                          "email": "om@test.com",
                          "password": "password1"
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Username already exists"));
    }

    @Test
    void register_duplicateEmail_returns409() throws Exception {
        when(userService.registerUser(any())).thenThrow(new DuplicateResourceException("Email already exists"));

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "omuser",
                          "email": "om@test.com",
                          "password": "password1"
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already exists"));
    }

    @Test
    void register_invalidUsername_returns400() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "om",
                          "email": "om@test.com",
                          "password": "password1"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists());
    }

    @Test
    void register_invalidEmail_returns400() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "omuser",
                          "email": "not-an-email",
                          "password": "password1"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void register_invalidPassword_returns400() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "omuser",
                          "email": "om@test.com",
                          "password": "short"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void register_blankFields_returns400() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "",
                          "email": "",
                          "password": ""
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void login_success_returns200WithoutPassword() throws Exception {
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken("jwt-token");
        response.setId(1);
        response.setUsername("omuser");
        response.setRole(Role.USER);
        when(userService.loginUser(any())).thenReturn(response);

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "omuser",
                          "password": "password1"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.username").value("omuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void login_invalidCredentials_returns401() throws Exception {
        when(userService.loginUser(any())).thenThrow(new InvalidCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "missing",
                          "password": "password1"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    void login_blankUsername_returns400() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "",
                          "password": "password1"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists());
    }

    @Test
    void login_blankPassword_returns400() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "username": "omuser",
                          "password": ""
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }
}
