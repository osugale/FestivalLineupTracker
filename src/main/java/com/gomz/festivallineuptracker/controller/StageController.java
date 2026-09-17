package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.dto.StageRequestDTO;
import com.gomz.festivallineuptracker.dto.StageResponseDTO;
import com.gomz.festivallineuptracker.service.StageService;
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
public class StageController {

    private final StageService stageService;

    public StageController(StageService stageService) {
        this.stageService = stageService;
    }

    @GetMapping("/festivals/{festivalId}/stages")
    @Operation(summary = "Get all stages of a festival")
    public ResponseEntity<List<StageResponseDTO>> getStagesForFestival(@PathVariable int festivalId) {
        return ResponseEntity.ok(stageService.getStagesForFestival(festivalId));
    }

    @GetMapping("/stages/{id}")
    @Operation(summary = "Get stage by ID")
    public ResponseEntity<StageResponseDTO> getPublicStageById(@PathVariable int id) {
        return ResponseEntity.ok(stageService.getStageById(id));
    }

    @PostMapping("/admin/stages")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new stage")
    public ResponseEntity<StageResponseDTO> createStage(@Valid @RequestBody StageRequestDTO request) {
        return ResponseEntity.status(201).body(stageService.createStage(request));
    }

    @GetMapping("/admin/stages/{id}")
    @Operation(summary = "Get stage by ID")
    public ResponseEntity<StageResponseDTO> getStageById(@PathVariable int id) {
        return ResponseEntity.ok(stageService.getStageById(id));
    }

    @GetMapping("/admin/stages")
    @Operation(summary = "Get all stages")
    public ResponseEntity<List<StageResponseDTO>> getAllStages() {
        return ResponseEntity.ok(stageService.getAllStages());
    }

    @PutMapping("/admin/stages/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a stage")
    public ResponseEntity<StageResponseDTO> updateStage(@PathVariable int id, @Valid @RequestBody StageRequestDTO request) {
        return ResponseEntity.ok(stageService.updateStage(id, request));
    }

    @DeleteMapping("/admin/stages/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a stage")
    public ResponseEntity<Void> deleteStage(@PathVariable int id) {
        stageService.deleteStage(id);
        return ResponseEntity.noContent().build();
    }
}
