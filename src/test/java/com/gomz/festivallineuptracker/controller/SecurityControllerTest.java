package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.config.SecurityConfig;
import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.dto.StageResponseDTO;
import com.gomz.festivallineuptracker.security.JwtAuthenticationFilter;
import com.gomz.festivallineuptracker.service.ArtistService;
import com.gomz.festivallineuptracker.service.FestivalService;
import com.gomz.festivallineuptracker.service.JwtService;
import com.gomz.festivallineuptracker.service.PerformanceService;
import com.gomz.festivallineuptracker.service.StageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ArtistController.class, FestivalController.class, StageController.class, PerformanceController.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})

class SecurityControllerTest {

    private static final String ARTIST_JSON =
            """
            {
              "name": "Test Artist",
              "genre": "Rock",
              "country": "UK"
            }
            """;

    private static final String FESTIVAL_JSON =
            """
            {
              "name": "Test Festival",
              "city": "Lisbon",
              "country": "Portugal",
              "venue": "Parque"
            }
            """;

    private static final String STAGE_JSON =
            """
            {
              "festivalId": 1,
              "name": "Main Stage"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistService artistService;

    @MockitoBean
    private FestivalService festivalService;

    @MockitoBean
    private StageService stageService;

    @MockitoBean
    private PerformanceService performanceService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;





    @Test
    void getArtists_withoutJwt_returns200() throws Exception {
        when(artistService.getArtists(anyInt(), anyInt(), anyString())).thenReturn(Page.empty());

        mockMvc.perform(get("/artists")).andExpect(status().isOk());
    }




    @Test
    void postArtists_withoutJwt_returns401() throws Exception {
        mockMvc.perform(post("/artists").contentType(MediaType.APPLICATION_JSON).content(ARTIST_JSON)).andExpect(status().isUnauthorized());
    }




    @Test
    @WithMockUser(roles = "USER")
    void postArtists_withUserRole_returns403() throws Exception {
        mockMvc.perform(post("/artists").contentType(MediaType.APPLICATION_JSON).content(ARTIST_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postArtists_withAdminRole_isAllowed() throws Exception {
        when(artistService.addArtist(any())).thenReturn(
                new ArtistResponseDTO("Test Artist", "Rock", "UK", null, null, null, null, null, null, 1)
        );

        mockMvc.perform(post("/artists").contentType(MediaType.APPLICATION_JSON).content(ARTIST_JSON)).andExpect(status().isCreated());
    }

    @Test
    void getFestivals_withoutJwt_returns200() throws Exception {
        when(festivalService.getFestivals(anyInt(), anyInt())).thenReturn(Page.empty());

        mockMvc.perform(get("/festivals")).andExpect(status().isOk());
    }

    @Test
    void postFestivals_withoutJwt_returns401() throws Exception {
        mockMvc.perform(post("/festivals").contentType(MediaType.APPLICATION_JSON).content(FESTIVAL_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void postFestivals_withUserRole_returns403() throws Exception {
        mockMvc.perform(post("/festivals").contentType(MediaType.APPLICATION_JSON).content(FESTIVAL_JSON)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postFestivals_withAdminRole_isAllowed() throws Exception {
        when(festivalService.addFestival(any())).thenReturn(
                new FestivalResponseDTO(1, "Test Festival", "Lisbon", "Portugal", "Parque", null, null, null, null, null, "Rock")
        );

        mockMvc.perform(post("/festivals").contentType(MediaType.APPLICATION_JSON).content(FESTIVAL_JSON)).andExpect(status().isCreated());
    }

    @Test
    void putArtists_withoutJwt_returns401() throws Exception {
        mockMvc.perform(put("/artists/1").contentType(MediaType.APPLICATION_JSON).content(ARTIST_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteArtists_withoutJwt_returns401() throws Exception {
        mockMvc.perform(delete("/artists/1")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void putArtists_withUserRole_returns403() throws Exception {
        mockMvc.perform(put("/artists/1").contentType(MediaType.APPLICATION_JSON).content(ARTIST_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteArtists_withUserRole_returns403() throws Exception {
        mockMvc.perform(delete("/artists/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void putArtists_withAdminRole_isAllowed() throws Exception {
        when(artistService.updateArtist(anyInt(), any())).thenReturn(
                new ArtistResponseDTO("Test Artist", "Rock", "UK", null, null, null, null, null, null, 1)
        );

        mockMvc.perform(put("/artists/1").contentType(MediaType.APPLICATION_JSON).content(ARTIST_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteArtists_withAdminRole_isAllowed() throws Exception {
        when(artistService.deleteArtist(1)).thenReturn(true);

        mockMvc.perform(delete("/artists/1")).andExpect(status().isNoContent());
    }

    @Test
    void putFestivals_withoutJwt_returns401() throws Exception {
        mockMvc.perform(put("/festivals/1").contentType(MediaType.APPLICATION_JSON).content(FESTIVAL_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteFestivals_withoutJwt_returns401() throws Exception {
        mockMvc.perform(delete("/festivals/1")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void putFestivals_withUserRole_returns403() throws Exception {
        mockMvc.perform(put("/festivals/1").contentType(MediaType.APPLICATION_JSON).content(FESTIVAL_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteFestivals_withUserRole_returns403() throws Exception {
        mockMvc.perform(delete("/festivals/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void putFestivals_withAdminRole_isAllowed() throws Exception {
        when(festivalService.updateFestival(anyInt(), any())).thenReturn(
                new FestivalResponseDTO(1, "Test Festival", "Lisbon", "Portugal", "Parque", null, null, null, null, null, "Rock")
        );

        mockMvc.perform(put("/festivals/1").contentType(MediaType.APPLICATION_JSON).content(FESTIVAL_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteFestivals_withAdminRole_isAllowed() throws Exception {
        when(festivalService.deleteFestival(1)).thenReturn(true);

        mockMvc.perform(delete("/festivals/1")).andExpect(status().isNoContent());
    }

    @Test
    void getAdminStages_withoutJwt_returns401() throws Exception {
        mockMvc.perform(get("/admin/stages")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAdminStages_withUserRole_isAllowed() throws Exception {
        when(stageService.getAllStages()).thenReturn(List.of());

        mockMvc.perform(get("/admin/stages")).andExpect(status().isOk());
    }

    @Test
    void postStages_withoutJwt_returns401() throws Exception {
        mockMvc.perform(post("/admin/stages").contentType(MediaType.APPLICATION_JSON).content(STAGE_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void putStages_withoutJwt_returns401() throws Exception {
        mockMvc.perform(put("/admin/stages/10").contentType(MediaType.APPLICATION_JSON).content(STAGE_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteStages_withoutJwt_returns401() throws Exception {
        mockMvc.perform(delete("/admin/stages/10")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void postStages_withUserRole_returns403() throws Exception {
        mockMvc.perform(post("/admin/stages").contentType(MediaType.APPLICATION_JSON).content(STAGE_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void putStages_withUserRole_returns403() throws Exception {
        mockMvc.perform(put("/admin/stages/10").contentType(MediaType.APPLICATION_JSON).content(STAGE_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteStages_withUserRole_returns403() throws Exception {
        mockMvc.perform(delete("/admin/stages/10")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postStages_withAdminRole_isAllowed() throws Exception {
        when(stageService.createStage(any())).thenReturn(new StageResponseDTO(10, 1, "Boom Festival", "Main Stage"));

        mockMvc.perform(post("/admin/stages").contentType(MediaType.APPLICATION_JSON).content(STAGE_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void putStages_withAdminRole_isAllowed() throws Exception {
        when(stageService.updateStage(anyInt(), any())).thenReturn(new StageResponseDTO(10, 1, "Boom Festival", "Main Stage"));

        mockMvc.perform(put("/admin/stages/10").contentType(MediaType.APPLICATION_JSON).content(STAGE_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStages_withAdminRole_isAllowed() throws Exception {
        mockMvc.perform(delete("/admin/stages/10")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getArtists_withUserRole_returns200() throws Exception {
        when(artistService.getArtists(anyInt(), anyInt(), anyString())).thenReturn(Page.empty());

        mockMvc.perform(get("/artists")).andExpect(status().isOk());
    }
}
