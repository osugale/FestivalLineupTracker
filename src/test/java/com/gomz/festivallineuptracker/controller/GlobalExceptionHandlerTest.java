package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.config.SecurityConfig;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.exception.ScheduleConflictException;
import com.gomz.festivallineuptracker.security.JwtAuthenticationFilter;
import com.gomz.festivallineuptracker.service.JwtService;
import com.gomz.festivallineuptracker.service.PerformanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PerformanceController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class GlobalExceptionHandlerTest {

    private static final String PERFORMANCE_JSON = """
            {
              "festivalId": 1,
              "artistId": 20,
              "stageId": 10,
              "startsAt": "2026-08-01T18:00:00",
              "endsAt": "2026-08-01T19:30:00",
              "scheduleStatus": "SCHEDULED"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PerformanceService performanceService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void resourceNotFound_returns404WithErrorField() throws Exception {
        when(performanceService.getPerformanceById(99))
                .thenThrow(new ResourceNotFoundException("Performance with id 99 not found"));

        mockMvc.perform(get("/performances/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Performance with id 99 not found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void invalidRequest_returns400WithErrorField() throws Exception {
        when(performanceService.createPerformance(any()))
                .thenThrow(new InvalidRequestException("Stage does not belong to the selected festival"));

        mockMvc.perform(post("/performances").contentType(MediaType.APPLICATION_JSON).content(PERFORMANCE_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Stage does not belong to the selected festival"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void scheduleConflict_returns409WithErrorField() throws Exception {
        when(performanceService.createPerformance(any()))
                .thenThrow(new ScheduleConflictException("Stage already has a performance overlapping this time"));

        mockMvc.perform(post("/performances").contentType(MediaType.APPLICATION_JSON).content(PERFORMANCE_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Stage already has a performance overlapping this time"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void validationFailure_returns400WithFieldMessages() throws Exception {
        mockMvc.perform(post("/performances").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.festivalId").exists())
                .andExpect(jsonPath("$.artistId").exists());
    }
}
