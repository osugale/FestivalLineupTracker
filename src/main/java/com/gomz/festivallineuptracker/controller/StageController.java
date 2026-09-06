package com.gomz.festivallineuptracker.controller;

import com.gomz.festivallineuptracker.dto.StageRequestDTO;
import com.gomz.festivallineuptracker.dto.StageResponseDTO;
import com.gomz.festivallineuptracker.service.StageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/stages")
public class StageController {

    private final StageService stageService;

    public StageController(StageService stageService) {
        this.stageService = stageService;
    }




    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new stage")
    public ResponseEntity<StageResponseDTO> createStage(@Valid @RequestBody StageRequestDTO request) {
        return ResponseEntity.status(201).body(stageService.createStage(request));
    }






    @GetMapping("/{id}")
    @Operation(summary = "Get stage by ID")
    public ResponseEntity<StageResponseDTO> getStageById(@PathVariable int id) {
        return ResponseEntity.ok(stageService.getStageById(id));
    }







    @GetMapping
    @Operation(summary = "Get all stages")
    public ResponseEntity<List<StageResponseDTO>> getAllStages() {
        return ResponseEntity.ok(stageService.getAllStages());
    }







    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a stage")
    public ResponseEntity<StageResponseDTO> updateStage(@PathVariable int id, @Valid @RequestBody StageRequestDTO request) {
        return ResponseEntity.ok(stageService.updateStage(id, request));
    }






    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a stage")
    public ResponseEntity<Void> deleteStage(@PathVariable int id) {
        stageService.deleteStage(id);
        return ResponseEntity.noContent().build();
    }
}
