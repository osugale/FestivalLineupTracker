package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.StageRequestDTO;
import com.gomz.festivallineuptracker.dto.StageResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.model.Stage;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.StageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StageServiceTest {

    @Mock
    private StageRepository stageRepository;

    @Mock
    private FestivalRepository festivalRepository;

    @InjectMocks
    private StageService stageService;

    private Festival festival;
    private Stage stage;

    @BeforeEach
    void setUp() {
        festival = new Festival();
        ReflectionTestUtils.setField(festival, "id", 1);
        festival.setName("Boom Festival");

        stage = new Stage(festival, "Main Stage");
        ReflectionTestUtils.setField(stage, "id", 10);
    }

    @Test
    void createStage_validRequest_returnsDto() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(stageRepository.existsByFestival_IdAndName(1, "Main Stage")).thenReturn(false);
        when(stageRepository.save(any(Stage.class))).thenAnswer(invocation -> {
            Stage saved = invocation.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 10);
            return saved;
        });

        StageResponseDTO result = stageService.createStage(request(1, "Main Stage"));

        assertEquals(10, result.getId());
        assertEquals(1, result.getFestivalId());
        assertEquals("Boom Festival", result.getFestivalName());
        assertEquals("Main Stage", result.getName());
    }

    @Test
    void getStageById_found_returnsDto() {
        when(stageRepository.findById(10)).thenReturn(Optional.of(stage));

        StageResponseDTO result = stageService.getStageById(10);

        assertEquals(10, result.getId());
        assertEquals(1, result.getFestivalId());
        assertEquals("Boom Festival", result.getFestivalName());
        assertEquals("Main Stage", result.getName());
    }

    @Test
    void getAllStages_returnsList() {
        when(stageRepository.findAll()).thenReturn(List.of(stage));

        List<StageResponseDTO> result = stageService.getAllStages();

        assertEquals(1, result.size());
        assertEquals("Main Stage", result.getFirst().getName());
    }

    @Test
    void updateStage_validRequest_savesChanges() {
        when(stageRepository.findById(10)).thenReturn(Optional.of(stage));
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(stageRepository.existsByFestival_IdAndNameAndIdNot(1, "Side Stage", 10)).thenReturn(false);
        when(stageRepository.save(stage)).thenReturn(stage);

        StageResponseDTO result = stageService.updateStage(10, request(1, "Side Stage"));

        assertEquals("Side Stage", result.getName());
        verify(stageRepository).save(stage);
    }

    @Test
    void deleteStage_existing_deletes() {
        when(stageRepository.existsById(10)).thenReturn(true);

        stageService.deleteStage(10);

        verify(stageRepository).deleteById(10);
    }

    @Test
    void deleteStage_missing_throwsNotFound() {
        when(stageRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> stageService.deleteStage(99));
        verify(stageRepository, never()).deleteById(99);
    }

    @Test
    void getStageById_missing_throwsNotFound() {
        when(stageRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stageService.getStageById(99));
    }

    @Test
    void createStage_missingFestival_throwsNotFound() {
        when(festivalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stageService.createStage(request(1, "Main Stage")));
        verify(stageRepository, never()).save(any());
    }

    @Test
    void createStage_duplicateNameSameFestival_throwsConflict() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(stageRepository.existsByFestival_IdAndName(1, "Main Stage")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> stageService.createStage(request(1, "Main Stage")));
    }

    @Test
    void createStage_sameNameDifferentFestival_isAllowed() {
        Festival otherFestival = new Festival();
        ReflectionTestUtils.setField(otherFestival, "id", 2);
        otherFestival.setName("Sziget");

        when(festivalRepository.findById(2)).thenReturn(Optional.of(otherFestival));
        when(stageRepository.existsByFestival_IdAndName(2, "Main Stage")).thenReturn(false);
        when(stageRepository.save(any(Stage.class))).thenAnswer(invocation -> {
            Stage saved = invocation.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 11);
            return saved;
        });

        StageResponseDTO result = stageService.createStage(request(2, "Main Stage"));

        assertEquals(2, result.getFestivalId());
        assertEquals("Main Stage", result.getName());
        verify(stageRepository).save(any(Stage.class));
    }

    private StageRequestDTO request(int festivalId, String name) {
        StageRequestDTO dto = new StageRequestDTO();
        dto.setFestivalId(festivalId);
        dto.setName(name);
        return dto;
    }
}
