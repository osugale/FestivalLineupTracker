package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.dto.ArtistRequestDTO;
import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }








    @GetMapping @Operation(summary = "Get all artists")
    public ResponseEntity<Page<ArtistResponseDTO>> getArtists(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size,
                                                              @RequestParam (defaultValue = "name") String sortBy) {

        return ResponseEntity.ok(artistService.getArtists(page, size, sortBy));
    }







    @GetMapping("/{id}")  @Operation(summary = "Get artist by ID")
    public ResponseEntity<ArtistResponseDTO> getArtistById(@PathVariable int id) {

        ArtistResponseDTO artist = artistService.getArtistById(id);

        if (artist != null) {
            return ResponseEntity.ok(artist);
        }

        return ResponseEntity.notFound().build();
    }









    @PostMapping   @Operation(summary = "Create a new artist")
    public ResponseEntity<ArtistResponseDTO> createArtist(@Valid @RequestBody ArtistRequestDTO artistDTO) {

        ArtistResponseDTO createdArtist = artistService.addArtist(artistDTO);

        return ResponseEntity.status(201).body(createdArtist);
    }











    @PutMapping("/{id}") @Operation(summary = "Update an artist")
    public ResponseEntity<ArtistResponseDTO> updateArtist(@PathVariable int id,@Valid @RequestBody ArtistRequestDTO dto) {

        ArtistResponseDTO artist = artistService.updateArtist(id, dto);

        if (artist != null) {
            return ResponseEntity.ok(artist);
        }

        return ResponseEntity.notFound().build();
    }







    @DeleteMapping("/{id}")    @Operation(summary = "Delete an artist")
    public ResponseEntity<Void> deleteArtist(@PathVariable int id) {

        boolean deleted = artistService.deleteArtist(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }








    @GetMapping("/{artistId}/festivals") @Operation(summary = "Get all festivals of an artist")
    public ResponseEntity<List<FestivalResponseDTO>> getFestivalsOfArtist(@PathVariable int artistId) {

        List<FestivalResponseDTO> festivals = artistService.getFestivalsOfArtist(artistId);

        return ResponseEntity.ok(festivals);
    }










    @GetMapping("/search") @Operation(summary = "Search artists by name")
    public ResponseEntity<List<ArtistResponseDTO>> searchArtists(@RequestParam String name) {

        return ResponseEntity.ok(artistService.searchArtists(name)
        );
    }
}