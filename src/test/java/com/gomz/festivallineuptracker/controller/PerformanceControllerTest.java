package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.config.SecurityConfig;
import com.gomz.festivallineuptracker.dto.PerformanceResponseDTO;
import com.gomz.festivallineuptracker.model.ScheduleStatus;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PerformanceController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class PerformanceControllerTest {

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
    void getPerformances_withoutJwt_returns200() throws Exception {
        when(performanceService.getAllPerformances()).thenReturn(List.of());

        mockMvc.perform(get("/performances")).andExpect(status().isOk());
    }

    @Test
    void postPerformances_withoutJwt_returns401() throws Exception {
        mockMvc.perform(post("/performances").contentType(MediaType.APPLICATION_JSON).content(PERFORMANCE_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void postPerformances_withUserRole_returns403() throws Exception {
        mockMvc.perform(post("/performances").contentType(MediaType.APPLICATION_JSON).content(PERFORMANCE_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postPerformances_withAdminRole_isAllowed() throws Exception {
        when(performanceService.createPerformance(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/performances").contentType(MediaType.APPLICATION_JSON).content(PERFORMANCE_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postPerformances_missingFestivalId_returns400() throws Exception {
        String invalidJson = """
                {
                  "artistId": 20,
                  "stageId": 10
                }
                """;

        mockMvc.perform(post("/performances").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFestivalPerformances_withoutJwt_returns200() throws Exception {
        when(performanceService.getPerformancesForFestival(anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/festivals/1/performances")).andExpect(status().isOk());
    }

    @Test
    void getStagePerformances_withoutJwt_returns200() throws Exception {
        when(performanceService.getPerformancesForStage(anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/stages/10/performances")).andExpect(status().isOk());
    }

    @Test
    void getArtistPerformances_withoutJwt_returns200() throws Exception {
        when(performanceService.getPerformancesForArtist(anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/artists/20/performances")).andExpect(status().isOk());
    }

    @Test
    void getPerformanceById_found_returnsDtoFields() throws Exception {
        when(performanceService.getPerformanceById(100)).thenReturn(sampleResponse());

        mockMvc.perform(get("/performances/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.festivalId").value(1))
                .andExpect(jsonPath("$.festivalName").value("Boom Festival"))
                .andExpect(jsonPath("$.stageId").value(10))
                .andExpect(jsonPath("$.stageName").value("Main Stage"))
                .andExpect(jsonPath("$.artistId").value(20))
                .andExpect(jsonPath("$.artistName").value("Four Tet"))
                .andExpect(jsonPath("$.scheduleStatus").value("SCHEDULED"))
                .andExpect(jsonPath("$.startsAt").exists())
                .andExpect(jsonPath("$.endsAt").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePerformance_withAdminRole_returns200() throws Exception {
        when(performanceService.updatePerformance(anyInt(), any())).thenReturn(sampleResponse());

        mockMvc.perform(put("/performances/100").contentType(MediaType.APPLICATION_JSON).content(PERFORMANCE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePerformance_withAdminRole_returns204() throws Exception {
        mockMvc.perform(delete("/performances/100")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePerformance_missing_returns404() throws Exception {
        doThrow(new com.gomz.festivallineuptracker.exception.ResourceNotFoundException("Performance with id 99 not found"))
                .when(performanceService).deletePerformance(99);

        mockMvc.perform(delete("/performances/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void putPerformances_withoutJwt_returns401() throws Exception {
        mockMvc.perform(put("/performances/100").contentType(MediaType.APPLICATION_JSON).content(PERFORMANCE_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deletePerformances_withoutJwt_returns401() throws Exception {
        mockMvc.perform(delete("/performances/100")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void putPerformances_withUserRole_returns403() throws Exception {
        mockMvc.perform(put("/performances/100").contentType(MediaType.APPLICATION_JSON).content(PERFORMANCE_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deletePerformances_withUserRole_returns403() throws Exception {
        mockMvc.perform(delete("/performances/100")).andExpect(status().isForbidden());
    }

    private PerformanceResponseDTO sampleResponse() {
        return new PerformanceResponseDTO(
                100, 1, "Boom Festival", 10, "Main Stage", 20, "Four Tet",
                LocalDateTime.of(2026, 8, 1, 18, 0),
                LocalDateTime.of(2026, 8, 1, 19, 30),
                ScheduleStatus.SCHEDULED
        );
    }
}
