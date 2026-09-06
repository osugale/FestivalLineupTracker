package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.dto.PerformanceRequestDTO;
import com.gomz.festivallineuptracker.dto.PerformanceResponseDTO;
import com.gomz.festivallineuptracker.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping("/performances")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new performance")
    public ResponseEntity<PerformanceResponseDTO> createPerformance(@Valid @RequestBody PerformanceRequestDTO request) {
        return ResponseEntity.status(201).body(performanceService.createPerformance(request));
    }

    @GetMapping("/performances")
    @Operation(summary = "Get all performances")
    public ResponseEntity<List<PerformanceResponseDTO>> getAllPerformances() {
        return ResponseEntity.ok(performanceService.getAllPerformances());
    }

    @GetMapping("/performances/{id}")
    @Operation(summary = "Get performance by ID")
    public ResponseEntity<PerformanceResponseDTO> getPerformanceById(@PathVariable int id) {
        return ResponseEntity.ok(performanceService.getPerformanceById(id));
    }

    @PutMapping("/performances/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a performance")
    public ResponseEntity<PerformanceResponseDTO> updatePerformance(@PathVariable int id,
                                                                    @Valid @RequestBody PerformanceRequestDTO request) {
        return ResponseEntity.ok(performanceService.updatePerformance(id, request));
    }

    @DeleteMapping("/performances/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a performance")
    public ResponseEntity<Void> deletePerformance(@PathVariable int id) {
        performanceService.deletePerformance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/festivals/{festivalId}/performances")
    @Operation(summary = "Get all performances of a festival")
    public ResponseEntity<List<PerformanceResponseDTO>> getPerformancesForFestival(@PathVariable int festivalId) {
        return ResponseEntity.ok(performanceService.getPerformancesForFestival(festivalId));
    }

    @GetMapping("/stages/{stageId}/performances")
    @Operation(summary = "Get all performances of a stage")
    public ResponseEntity<List<PerformanceResponseDTO>> getPerformancesForStage(@PathVariable int stageId) {
        return ResponseEntity.ok(performanceService.getPerformancesForStage(stageId));
    }

    @GetMapping("/artists/{artistId}/performances")
    @Operation(summary = "Get all performances of an artist")
    public ResponseEntity<List<PerformanceResponseDTO>> getPerformancesForArtist(@PathVariable int artistId) {
        return ResponseEntity.ok(performanceService.getPerformancesForArtist(artistId));
    }
}
