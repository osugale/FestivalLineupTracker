package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.config.SecurityConfig;
import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.GenreResponseDTO;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.security.JwtAuthenticationFilter;
import com.gomz.festivallineuptracker.service.JwtService;
import com.gomz.festivallineuptracker.service.UserPreferenceService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserPreferenceController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class UserPreferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserPreferenceService userPreferenceService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void getGenrePreferences_withoutJwt_returns401() throws Exception {
        mockMvc.perform(get("/me/genre-preferences")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getOnboardingArtists_withoutSavedGenres_returns400() throws Exception {
        when(userPreferenceService.getOnboardingArtists())
                .thenThrow(new InvalidRequestException("Select genres first"));

        mockMvc.perform(get("/me/onboarding/artists"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Select genres first"));
    }

    @Test
    @WithMockUser
    void putFavoriteArtists_emptyList_returns200() throws Exception {
        when(userPreferenceService.replaceFavoriteArtists(any())).thenReturn(List.of());

        mockMvc.perform(put("/me/favorite-artists").contentType(MediaType.APPLICATION_JSON).content("""
                { "artistIds": [] }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser
    void putGenrePreferences_returnsSavedParents() throws Exception {
        when(userPreferenceService.replaceGenrePreferences(any())).thenReturn(List.of(
                new GenreResponseDTO(1, "Electronic", "electronic"),
                new GenreResponseDTO(2, "Hip Hop", "hip-hop"),
                new GenreResponseDTO(3, "Rock", "rock")
        ));

        mockMvc.perform(put("/me/genre-preferences").contentType(MediaType.APPLICATION_JSON).content("""
                { "genreIds": [1, 2, 3] }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronic"));
    }

    @Test
    @WithMockUser
    void getOnboardingArtists_returnsArtists() throws Exception {
        when(userPreferenceService.getOnboardingArtists()).thenReturn(List.of(
                new ArtistResponseDTO(1, "Four Tet", "four-tet", "UK", null, null, null, null, null, null, List.of())
        ));

        mockMvc.perform(get("/me/onboarding/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Four Tet"));
    }
}
