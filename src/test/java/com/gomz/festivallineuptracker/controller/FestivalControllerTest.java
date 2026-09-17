package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.config.SecurityConfig;
import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.security.JwtAuthenticationFilter;
import com.gomz.festivallineuptracker.service.FestivalService;
import com.gomz.festivallineuptracker.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FestivalController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class FestivalControllerTest {

    private static final String FESTIVAL_JSON = """
            {
              "name": "Boom Festival",
              "city": "Idanha",
              "country": "Portugal",
              "venue": "Idanha-a-Nova",
              "startDate": "2026-07-18",
              "endDate": "2026-07-25",
              "timezone": "Europe/Lisbon"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FestivalService festivalService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void getFestivals_returnsPageOfDtos() throws Exception {
        when(festivalService.getFestivals(anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of(sampleFestival())));

        mockMvc.perform(get("/festivals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Boom Festival"));
    }

    @Test
    void getFestivalById_found_returnsDto() throws Exception {
        when(festivalService.getFestivalById(1)).thenReturn(sampleFestival());

        mockMvc.perform(get("/festivals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Boom Festival"))
                .andExpect(jsonPath("$.city").value("Idanha"))
                .andExpect(jsonPath("$.country").value("Portugal"))
                .andExpect(jsonPath("$.venue").value("Idanha-a-Nova"));
    }

    @Test
    void getFestivalById_missing_returns404() throws Exception {
        when(festivalService.getFestivalById(99)).thenThrow(new ResourceNotFoundException("Festival with id 99 not found"));

        mockMvc.perform(get("/festivals/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Festival with id 99 not found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createFestival_validRequest_returns201() throws Exception {
        when(festivalService.addFestival(any())).thenReturn(sampleFestival());

        mockMvc.perform(post("/festivals").contentType(MediaType.APPLICATION_JSON).content(FESTIVAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createFestival_invalidRequest_returns400() throws Exception {
        String invalidJson = """
                {
                  "name": "",
                  "city": "Idanha",
                  "country": "Portugal",
                  "venue": "Idanha-a-Nova"
                }
                """;

        mockMvc.perform(post("/festivals").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.timezone").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createFestival_endBeforeStart_returns400() throws Exception {
        String invalidJson = """
                {
                  "name": "Boom Festival",
                  "city": "Idanha",
                  "country": "Portugal",
                  "venue": "Idanha-a-Nova",
                  "startDate": "2026-07-25",
                  "endDate": "2026-07-18",
                  "timezone": "Europe/Lisbon"
                }
                """;

        mockMvc.perform(post("/festivals").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateFestival_found_returns200() throws Exception {
        when(festivalService.updateFestival(eq(1), any())).thenReturn(sampleFestival());

        mockMvc.perform(put("/festivals/1").contentType(MediaType.APPLICATION_JSON).content(FESTIVAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateFestival_missing_returns404() throws Exception {
        when(festivalService.updateFestival(eq(99), any()))
                .thenThrow(new ResourceNotFoundException("Festival with id 99 not found"));

        mockMvc.perform(put("/festivals/99").contentType(MediaType.APPLICATION_JSON).content(FESTIVAL_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFestival_existing_returns204() throws Exception {
        when(festivalService.deleteFestival(1)).thenReturn(true);

        mockMvc.perform(delete("/festivals/1")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFestival_missing_returns404() throws Exception {
        when(festivalService.deleteFestival(99)).thenReturn(false);

        mockMvc.perform(delete("/festivals/99")).andExpect(status().isNotFound());
    }

    @Test
    void getArtistsOfFestival_returnsList() throws Exception {
        when(festivalService.getArtistsOfFestival(1)).thenReturn(List.of(
                new ArtistResponseDTO(20, "Four Tet", "four-tet", "UK", null, null, null, null, null, null, List.of())
        ));

        mockMvc.perform(get("/festivals/1/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(20))
                .andExpect(jsonPath("$[0].name").value("Four Tet"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addArtistToFestival_returns200() throws Exception {
        when(festivalService.addArtistToFestival(1, 20)).thenReturn(sampleFestival());

        mockMvc.perform(post("/festivals/1/artists/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addArtistToFestival_missing_returns404() throws Exception {
        when(festivalService.addArtistToFestival(1, 20))
                .thenThrow(new ResourceNotFoundException("Either Festival or Artist with id 120 not found"));

        mockMvc.perform(post("/festivals/1/artists/20"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeArtistFromFestival_existing_returns204() throws Exception {
        when(festivalService.deleteArtistFestival(1, 20)).thenReturn(true);

        mockMvc.perform(delete("/festivals/1/artists/20")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeArtistFromFestival_missing_returns404() throws Exception {
        when(festivalService.deleteArtistFestival(1, 20)).thenReturn(false);

        mockMvc.perform(delete("/festivals/1/artists/20")).andExpect(status().isNotFound());
    }

    private FestivalResponseDTO sampleFestival() {
        return new FestivalResponseDTO(1, "Boom Festival", "Idanha", "Portugal", "Idanha-a-Nova",
                java.time.LocalDate.of(2026, 7, 18), java.time.LocalDate.of(2026, 7, 25), "Europe/Lisbon",
                null, null, null, null, List.of());
    }
}
