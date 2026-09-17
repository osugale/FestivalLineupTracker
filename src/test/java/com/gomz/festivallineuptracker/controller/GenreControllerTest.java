package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.config.SecurityConfig;
import com.gomz.festivallineuptracker.dto.GenreRelationResponseDTO;
import com.gomz.festivallineuptracker.dto.GenreResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.security.JwtAuthenticationFilter;
import com.gomz.festivallineuptracker.service.GenreService;
import com.gomz.festivallineuptracker.service.JwtService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void getGenres_withoutJwt_returns200() throws Exception {
        when(genreService.getGenres()).thenReturn(List.of(new GenreResponseDTO(1, "Techno", "techno")));

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Techno"))
                .andExpect(jsonPath("$[0].slug").value("techno"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGenre_validRequest_returns201() throws Exception {
        when(genreService.createGenre(any())).thenReturn(new GenreResponseDTO(1, "Techno", "techno"));

        mockMvc.perform(post("/genres").contentType(MediaType.APPLICATION_JSON).content("""
                { "name": "Techno" }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("techno"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGenre_duplicate_returns409() throws Exception {
        when(genreService.createGenre(any())).thenThrow(new DuplicateResourceException("Genre with name 'Techno' already exists"));

        mockMvc.perform(post("/genres").contentType(MediaType.APPLICATION_JSON).content("""
                { "name": "Techno" }
                """))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createRelation_returns201() throws Exception {
        when(genreService.createRelation(eq(1), eq(2))).thenReturn(new GenreRelationResponseDTO(
                9,
                new GenreResponseDTO(1, "Drum & Bass", "drum-and-bass"),
                new GenreResponseDTO(2, "Jungle", "jungle")
        ));

        mockMvc.perform(post("/genres/1/relations/2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parentGenre.name").value("Drum & Bass"))
                .andExpect(jsonPath("$.childGenre.name").value("Jungle"))
                .andExpect(jsonPath("$.affinity").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRelation_returns204() throws Exception {
        mockMvc.perform(delete("/genre-relations/9")).andExpect(status().isNoContent());
    }
}
