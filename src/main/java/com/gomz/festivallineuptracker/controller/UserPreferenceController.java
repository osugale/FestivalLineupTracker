package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FavoriteArtistsRequestDTO;
import com.gomz.festivallineuptracker.dto.GenrePreferenceRequestDTO;
import com.gomz.festivallineuptracker.dto.GenreResponseDTO;
import com.gomz.festivallineuptracker.service.UserPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;



    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }





    @GetMapping("/me/genre-preferences")
    @Operation(summary = "Get saved parent genre preferences")
    public ResponseEntity<List<GenreResponseDTO>> getGenrePreferences() {
        return ResponseEntity.ok(userPreferenceService.getGenrePreferences());
    }





    @PutMapping("/me/genre-preferences")
    @Operation(summary = "Replace parent genre preferences (3 to 5)")
    public ResponseEntity<List<GenreResponseDTO>> replaceGenrePreferences(
            @Valid @RequestBody GenrePreferenceRequestDTO request) {
        return ResponseEntity.ok(userPreferenceService.replaceGenrePreferences(request));
    }








    @GetMapping("/me/onboarding/artists")
    @Operation(summary = "List artists eligible for onboarding favorites")
    public ResponseEntity<List<ArtistResponseDTO>> getOnboardingArtists() {
        return ResponseEntity.ok(userPreferenceService.getOnboardingArtists());
    }





    @PutMapping("/me/onboarding/favorite-artists")
    @Operation(summary = "Replace onboarding favorite artists (exactly 5 eligible)")
    public ResponseEntity<List<ArtistResponseDTO>> replaceOnboardingFavorites(
            @Valid @RequestBody FavoriteArtistsRequestDTO request) {
        return ResponseEntity.ok(userPreferenceService.replaceOnboardingFavorites(request));
    }






    @GetMapping("/me/favorite-artists")
    @Operation(summary = "Get favorite artists")
    public ResponseEntity<List<ArtistResponseDTO>> getFavoriteArtists() {
        return ResponseEntity.ok(userPreferenceService.getFavoriteArtists());
    }






    @PutMapping("/me/favorite-artists")
    @Operation(summary = "Replace favorite artists")
    public ResponseEntity<List<ArtistResponseDTO>> replaceFavoriteArtists(
            @Valid @RequestBody FavoriteArtistsRequestDTO request) {
        return ResponseEntity.ok(userPreferenceService.replaceFavoriteArtists(request));
    }
}
