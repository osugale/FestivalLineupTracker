package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.config.SecurityConfig;
import com.gomz.festivallineuptracker.dto.StageResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.security.JwtAuthenticationFilter;
import com.gomz.festivallineuptracker.service.JwtService;
import com.gomz.festivallineuptracker.service.StageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StageController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class StageControllerTest {

    private static final String STAGE_JSON = """
            {
              "festivalId": 1,
              "name": "Main Stage"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StageService stageService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    void getAllStages_authenticated_returns200() throws Exception {
        when(stageService.getAllStages()).thenReturn(List.of(sampleStage()));

        mockMvc.perform(get("/admin/stages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].festivalId").value(1))
                .andExpect(jsonPath("$[0].festivalName").value("Boom Festival"))
                .andExpect(jsonPath("$[0].name").value("Main Stage"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getStageById_found_returnsDto() throws Exception {
        when(stageService.getStageById(10)).thenReturn(sampleStage());

        mockMvc.perform(get("/admin/stages/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.festivalId").value(1))
                .andExpect(jsonPath("$.festivalName").value("Boom Festival"))
                .andExpect(jsonPath("$.name").value("Main Stage"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getStageById_missing_returns404() throws Exception {
        when(stageService.getStageById(99)).thenThrow(new ResourceNotFoundException("Stage with id 99 not found"));

        mockMvc.perform(get("/admin/stages/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Stage with id 99 not found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createStage_validRequest_returns201() throws Exception {
        when(stageService.createStage(any())).thenReturn(sampleStage());

        mockMvc.perform(post("/admin/stages").contentType(MediaType.APPLICATION_JSON).content(STAGE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.festivalId").value(1))
                .andExpect(jsonPath("$.festivalName").value("Boom Festival"))
                .andExpect(jsonPath("$.name").value("Main Stage"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createStage_invalidRequest_returns400() throws Exception {
        String invalidJson = """
                {
                  "festivalId": 1,
                  "name": ""
                }
                """;

        mockMvc.perform(post("/admin/stages").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createStage_duplicate_returns409() throws Exception {
        when(stageService.createStage(any()))
                .thenThrow(new DuplicateResourceException("Stage with name 'Main Stage' already exists for this festival"));

        mockMvc.perform(post("/admin/stages").contentType(MediaType.APPLICATION_JSON).content(STAGE_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStage_validRequest_returns200() throws Exception {
        when(stageService.updateStage(eq(10), any())).thenReturn(sampleStage());

        mockMvc.perform(put("/admin/stages/10").contentType(MediaType.APPLICATION_JSON).content(STAGE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStage_existing_returns204() throws Exception {
        mockMvc.perform(delete("/admin/stages/10")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStage_missing_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Stage with id 99 not found")).when(stageService).deleteStage(99);

        mockMvc.perform(delete("/admin/stages/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    private StageResponseDTO sampleStage() {
        return new StageResponseDTO(10, 1, "Boom Festival", "Main Stage");
    }
}
