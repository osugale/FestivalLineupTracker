package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.dto.GenreRelationRequestDTO;
import com.gomz.festivallineuptracker.dto.GenreRelationResponseDTO;
import com.gomz.festivallineuptracker.dto.GenreRequestDTO;
import com.gomz.festivallineuptracker.dto.GenreResponseDTO;
import com.gomz.festivallineuptracker.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }






    @GetMapping("/genres")
    @Operation(summary = "Get all genres")
    public ResponseEntity<List<GenreResponseDTO>> getGenres() {return ResponseEntity.ok(genreService.getGenres());    }





    @GetMapping("/genres/{id}")
    @Operation(summary = "Get genre by ID")
    public ResponseEntity<GenreResponseDTO> getGenreById(@PathVariable int id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }








    @PostMapping("/genres")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a genre")
    public ResponseEntity<GenreResponseDTO> createGenre(@Valid @RequestBody GenreRequestDTO request) {
        return ResponseEntity.status(201).body(genreService.createGenre(request));
    }









    @PutMapping("/genres/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a genre")
    public ResponseEntity<GenreResponseDTO> updateGenre(@PathVariable int id, @Valid @RequestBody GenreRequestDTO request) {
        return ResponseEntity.ok(genreService.updateGenre(id, request));
    }









    @DeleteMapping("/genres/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a genre")
    public ResponseEntity<Void> deleteGenre(@PathVariable int id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }








    @GetMapping("/genres/{id}/relations")
    @Operation(summary = "Get child genres in a genre taxonomy")
    public ResponseEntity<List<GenreRelationResponseDTO>> getRelations(@PathVariable int id) {
        return ResponseEntity.ok(genreService.getRelationsForGenre(id));
    }







//    @PostMapping("/genres/{id}/relations")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Create a parent-to-child genre taxonomy relation")
//    public ResponseEntity<GenreRelationResponseDTO> createRelation(@PathVariable("id") int parentGenreId, @Valid @RequestBody GenreRelationRequestDTO request) {
//        return ResponseEntity.status(201).body(genreService.createRelation(parentGenreId, request));
//    }

    @PostMapping("/genres/{parentGenreId}/relations/{childGenreId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a parent-to-child genre taxonomy relation")
    public ResponseEntity<GenreRelationResponseDTO> createRelation(@PathVariable int parentGenreId, @PathVariable int childGenreId) {

        return ResponseEntity.status(201).body(genreService.createRelation(parentGenreId, childGenreId));
    }









    @DeleteMapping("/genre-relations/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a genre relation")
    public ResponseEntity<Void> deleteRelation(@PathVariable int id) {
        genreService.deleteRelation(id);
        return ResponseEntity.noContent().build();
    }
}
