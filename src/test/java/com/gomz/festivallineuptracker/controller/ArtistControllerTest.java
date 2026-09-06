package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.config.SecurityConfig;
import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.security.JwtAuthenticationFilter;
import com.gomz.festivallineuptracker.service.ArtistService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArtistController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class ArtistControllerTest {

    private static final String ARTIST_JSON = """
            {
              "name": "Four Tet",
              "genre": "Electronic",
              "country": "UK"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistService artistService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void getArtists_returnsPageOfDtos() throws Exception {
        when(artistService.getArtists(anyInt(), anyInt(), anyString()))
                .thenReturn(new PageImpl<>(List.of(sampleArtist())));

        mockMvc.perform(get("/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Four Tet"));
    }

    @Test
    void getArtistById_found_returnsDto() throws Exception {
        when(artistService.getArtistById(1)).thenReturn(sampleArtist());

        mockMvc.perform(get("/artists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Four Tet"))
                .andExpect(jsonPath("$.genre").value("Electronic"))
                .andExpect(jsonPath("$.country").value("UK"));
    }

    @Test
    void getArtistById_missing_returns404() throws Exception {
        when(artistService.getArtistById(99)).thenThrow(new ResourceNotFoundException("Artist with id 99 not found"));

        mockMvc.perform(get("/artists/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Artist with id 99 not found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createArtist_validRequest_returns201() throws Exception {
        when(artistService.addArtist(any())).thenReturn(sampleArtist());

        mockMvc.perform(post("/artists").contentType(MediaType.APPLICATION_JSON).content(ARTIST_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Four Tet"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createArtist_invalidRequest_returns400() throws Exception {
        String invalidJson = """
                {
                  "name": "",
                  "genre": "Electronic",
                  "country": "UK"
                }
                """;

        mockMvc.perform(post("/artists").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateArtist_found_returns200() throws Exception {
        when(artistService.updateArtist(eq(1), any())).thenReturn(sampleArtist());

        mockMvc.perform(put("/artists/1").contentType(MediaType.APPLICATION_JSON).content(ARTIST_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateArtist_missing_returns404() throws Exception {
        when(artistService.updateArtist(eq(99), any()))
                .thenThrow(new ResourceNotFoundException("Artist with id 99 not found"));

        mockMvc.perform(put("/artists/99").contentType(MediaType.APPLICATION_JSON).content(ARTIST_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteArtist_existing_returns204() throws Exception {
        when(artistService.deleteArtist(1)).thenReturn(true);

        mockMvc.perform(delete("/artists/1")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteArtist_missing_returns404() throws Exception {
        when(artistService.deleteArtist(99)).thenReturn(false);

        mockMvc.perform(delete("/artists/99")).andExpect(status().isNotFound());
    }

    @Test
    void getFestivalsOfArtist_returnsList() throws Exception {
        when(artistService.getFestivalsOfArtist(1)).thenReturn(List.of(
                new FestivalResponseDTO(8, "Boom Festival", "Idanha", "Portugal", "Idanha-a-Nova",
                        null, null, null, null, null, "Electronic")
        ));

        mockMvc.perform(get("/artists/1/festivals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(8))
                .andExpect(jsonPath("$[0].name").value("Boom Festival"));
    }

    private ArtistResponseDTO sampleArtist() {
        return new ArtistResponseDTO("Four Tet", "Electronic", "UK", null, null, null, null, null, null, 1);
    }
}
