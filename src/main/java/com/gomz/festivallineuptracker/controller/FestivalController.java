package com.gomz.festivallineuptracker.controller;


import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalRequestDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.service.FestivalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/festivals")
public class FestivalController {


    private final FestivalService festivalService;

    public FestivalController(FestivalService festivalService){

        this.festivalService=festivalService;

    }










    @GetMapping @Operation(summary = "Get all festivals")
    public ResponseEntity<Page<FestivalResponseDTO>> getFestivals(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size) {

        return ResponseEntity.ok(festivalService.getFestivals(page, size));
    }

    @GetMapping("/{id}") @Operation(summary = "Get festival by ID")
    public ResponseEntity<FestivalResponseDTO> getFestivalById(@PathVariable int id) {

        FestivalResponseDTO festival = festivalService.getFestivalById(id);

        if (festival != null) {
            return ResponseEntity.ok(festival);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping @Operation(summary = "Create a new festival")
    public ResponseEntity<FestivalResponseDTO> createFestival(
            @Valid @RequestBody FestivalRequestDTO dto) {

        FestivalResponseDTO festival = festivalService.addFestival(dto);

        return ResponseEntity.status(201).body(festival);
    }

    @PutMapping("/{id}") @Operation(summary = "Update a festival")
    public ResponseEntity<FestivalResponseDTO> updateFestival(
            @PathVariable int id,
            @Valid @RequestBody FestivalRequestDTO dto) {

        FestivalResponseDTO festival = festivalService.updateFestival(id, dto);

        if (festival != null) {
            return ResponseEntity.ok(festival);
        }

        return ResponseEntity.notFound().build();
    }



    @GetMapping("/{festivalId}/artists") @Operation(summary = "Get all artists of a festival")
    public ResponseEntity<List<ArtistResponseDTO>> getArtistsOfFestival(@PathVariable int festivalId) {

        List<ArtistResponseDTO> artists = festivalService.getArtistsOfFestival(festivalId);

        return ResponseEntity.ok(artists);
    }





    @PostMapping("/{festivalId}/artists/{artistId}") @Operation(summary = "Add an artist to a festival")
    public ResponseEntity<FestivalResponseDTO> createRelation(@PathVariable int festivalId, @PathVariable int artistId) {

        FestivalResponseDTO festival = festivalService.addArtistToFestival(festivalId, artistId);

        if (festival == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(festival);
    }




    @GetMapping("/search") @Operation(summary = "Search festivals by name")
    public ResponseEntity<List<FestivalResponseDTO>> searchFestivals(@RequestParam String name) {

        return ResponseEntity.ok(festivalService.searchFestivals(name));

    }


    @DeleteMapping("/{festivalId}/artists/{artistId}") @Operation(summary = "Remove an artist from a festival")
    public ResponseEntity<Void> deleteRelation(@PathVariable int festivalId, @PathVariable int artistId) {

        boolean deleted = festivalService.deleteArtistFestival(festivalId, artistId);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }





    @DeleteMapping("/{id}") @Operation(summary = "Delete a festival")
    public ResponseEntity<Void> deleteFestival(@PathVariable int id){

        boolean deleted =festivalService.deleteFestival(id);

        if(deleted){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}