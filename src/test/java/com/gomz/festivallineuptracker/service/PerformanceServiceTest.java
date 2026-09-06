package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.PerformanceRequestDTO;
import com.gomz.festivallineuptracker.dto.PerformanceResponseDTO;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.exception.ScheduleConflictException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.model.Performance;
import com.gomz.festivallineuptracker.model.ScheduleStatus;
import com.gomz.festivallineuptracker.model.Stage;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.PerformanceRepository;
import com.gomz.festivallineuptracker.repository.StageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceTest {

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private StageRepository stageRepository;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private PerformanceService performanceService;

    private Festival festival;
    private Stage stage;
    private Artist artist;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;

    @BeforeEach
    void setUp() {
        festival = new Festival();
        ReflectionTestUtils.setField(festival, "id", 1);
        festival.setName("Boom Festival");

        stage = new Stage(festival, "Main Stage");
        ReflectionTestUtils.setField(stage, "id", 10);

        artist = new Artist();
        artist.setId(20);
        artist.setName("Four Tet");

        startsAt = LocalDateTime.of(2026, 8, 1, 18, 0);
        endsAt = LocalDateTime.of(2026, 8, 1, 19, 30);
    }

    @Test
    void createPerformance_validRequest_savesPerformance() {
        stubLookups();
        when(performanceRepository.existsByStage_IdAndStartsAtLessThanAndEndsAtGreaterThan(10, endsAt, startsAt)).thenReturn(false);
        when(performanceRepository.existsByArtist_IdAndStartsAtLessThanAndEndsAtGreaterThan(20, endsAt, startsAt)).thenReturn(false);
        when(performanceRepository.save(any(Performance.class))).thenAnswer(invocation -> {
            Performance performance = invocation.getArgument(0);
            ReflectionTestUtils.setField(performance, "id", 100);
            return performance;
        });

        PerformanceResponseDTO response = performanceService.createPerformance(validRequest());

        assertEquals(100, response.getId());
        assertEquals(1, response.getFestivalId());
        assertEquals("Boom Festival", response.getFestivalName());
        assertEquals(10, response.getStageId());
        assertEquals("Main Stage", response.getStageName());
        assertEquals(20, response.getArtistId());
        assertEquals("Four Tet", response.getArtistName());
        assertEquals(ScheduleStatus.SCHEDULED, response.getScheduleStatus());
        verify(performanceRepository).save(any(Performance.class));
    }

    @Test
    void getPerformanceById_found_returnsDto() {
        when(performanceRepository.findById(100)).thenReturn(Optional.of(savedPerformance()));

        PerformanceResponseDTO response = performanceService.getPerformanceById(100);

        assertEquals(100, response.getId());
        assertEquals("Four Tet", response.getArtistName());
    }

    @Test
    void getAllPerformances_returnsMappedList() {
        when(performanceRepository.findAll()).thenReturn(List.of(savedPerformance()));

        List<PerformanceResponseDTO> response = performanceService.getAllPerformances();

        assertEquals(1, response.size());
        assertEquals(100, response.getFirst().getId());
    }

    @Test
    void updatePerformance_validRequest_savesChanges() {
        stubLookups();
        Performance existing = savedPerformance();
        when(performanceRepository.findById(100)).thenReturn(Optional.of(existing));
        when(performanceRepository.existsByStage_IdAndIdNotAndStartsAtLessThanAndEndsAtGreaterThan(10, 100, endsAt, startsAt)).thenReturn(false);
        when(performanceRepository.existsByArtist_IdAndIdNotAndStartsAtLessThanAndEndsAtGreaterThan(20, 100, endsAt, startsAt)).thenReturn(false);
        when(performanceRepository.save(existing)).thenReturn(existing);

        PerformanceResponseDTO response = performanceService.updatePerformance(100, validRequest());

        assertEquals(100, response.getId());
        verify(performanceRepository).save(existing);
    }

    @Test
    void deletePerformance_existingId_deletes() {
        when(performanceRepository.existsById(100)).thenReturn(true);

        performanceService.deletePerformance(100);

        verify(performanceRepository).deleteById(100);
    }

    @Test
    void createPerformance_missingFestival_throwsNotFound() {
        when(festivalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> performanceService.createPerformance(validRequest()));
        verify(performanceRepository, never()).save(any());
    }

    @Test
    void createPerformance_missingStage_throwsNotFound() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));
        when(stageRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> performanceService.createPerformance(validRequest()));
    }

    @Test
    void createPerformance_missingArtist_throwsNotFound() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(artistRepository.findById(20)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> performanceService.createPerformance(validRequest()));
    }

    @Test
    void createPerformance_stageFromAnotherFestival_rejected() {
        Festival otherFestival = new Festival();
        ReflectionTestUtils.setField(otherFestival, "id", 2);
        Stage otherStage = new Stage(otherFestival, "Other Stage");
        ReflectionTestUtils.setField(otherStage, "id", 10);

        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));
        when(stageRepository.findById(10)).thenReturn(Optional.of(otherStage));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> performanceService.createPerformance(validRequest()));

        assertEquals("Stage does not belong to the selected festival", exception.getMessage());
    }

    @Test
    void createPerformance_overlappingStage_rejected() {
        stubLookups();
        when(performanceRepository.existsByStage_IdAndStartsAtLessThanAndEndsAtGreaterThan(10, endsAt, startsAt)).thenReturn(true);

        assertThrows(ScheduleConflictException.class, () -> performanceService.createPerformance(validRequest()));
        verify(performanceRepository, never()).save(any());
    }

    @Test
    void createPerformance_overlappingArtist_rejected() {
        stubLookups();
        when(performanceRepository.existsByStage_IdAndStartsAtLessThanAndEndsAtGreaterThan(10, endsAt, startsAt)).thenReturn(false);
        when(performanceRepository.existsByArtist_IdAndStartsAtLessThanAndEndsAtGreaterThan(20, endsAt, startsAt)).thenReturn(true);

        assertThrows(ScheduleConflictException.class, () -> performanceService.createPerformance(validRequest()));
        verify(performanceRepository, never()).save(any());
    }

    @Test
    void getPerformancesForFestival_returnsList() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(performanceRepository.findByFestival_Id(1)).thenReturn(List.of(savedPerformance()));

        List<PerformanceResponseDTO> response = performanceService.getPerformancesForFestival(1);

        assertEquals(1, response.size());
    }

    @Test
    void getPerformancesForStage_returnsList() {
        when(stageRepository.findById(10)).thenReturn(Optional.of(stage));
        when(performanceRepository.findByStage_Id(10)).thenReturn(List.of(savedPerformance()));

        List<PerformanceResponseDTO> response = performanceService.getPerformancesForStage(10);

        assertEquals(1, response.size());
    }

    @Test
    void getPerformancesForArtist_returnsList() {
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));
        when(performanceRepository.findByArtist_Id(20)).thenReturn(List.of(savedPerformance()));

        List<PerformanceResponseDTO> response = performanceService.getPerformancesForArtist(20);

        assertEquals(1, response.size());
    }

    @Test
    void createPerformance_scheduledWithoutTimes_rejected() {
        stubLookups();
        PerformanceRequestDTO request = validRequest();
        request.setStartsAt(null);
        request.setEndsAt(null);

        assertThrows(InvalidRequestException.class, () -> performanceService.createPerformance(request));
    }

    @Test
    void createPerformance_tbaWithoutTimes_isAllowed() {
        stubLookups();
        when(performanceRepository.save(any(Performance.class))).thenAnswer(invocation -> {
            Performance performance = invocation.getArgument(0);
            ReflectionTestUtils.setField(performance, "id", 101);
            return performance;
        });

        PerformanceRequestDTO request = validRequest();
        request.setScheduleStatus(ScheduleStatus.TBA);
        request.setStartsAt(null);
        request.setEndsAt(null);

        PerformanceResponseDTO response = performanceService.createPerformance(request);

        assertEquals(101, response.getId());
        assertEquals(ScheduleStatus.TBA, response.getScheduleStatus());
        verify(performanceRepository, never()).existsByStage_IdAndStartsAtLessThanAndEndsAtGreaterThan(anyInt(), any(), any());
        verify(performanceRepository).save(any(Performance.class));
    }

    @Test
    void getPerformanceById_missing_throwsNotFound() {
        when(performanceRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> performanceService.getPerformanceById(99));
    }

    @Test
    void deletePerformance_missing_throwsNotFound() {
        when(performanceRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> performanceService.deletePerformance(99));
        verify(performanceRepository, never()).deleteById(99);
    }

    @Test
    void createPerformance_scheduledWithoutStartsAt_rejected() {
        stubLookups();
        PerformanceRequestDTO request = validRequest();
        request.setStartsAt(null);

        assertThrows(InvalidRequestException.class, () -> performanceService.createPerformance(request));
    }

    @Test
    void createPerformance_scheduledWithoutEndsAt_rejected() {
        stubLookups();
        PerformanceRequestDTO request = validRequest();
        request.setEndsAt(null);

        assertThrows(InvalidRequestException.class, () -> performanceService.createPerformance(request));
    }

    @Test
    void createPerformance_endsAtBeforeStartsAt_rejected() {
        stubLookups();
        PerformanceRequestDTO request = validRequest();
        request.setStartsAt(endsAt);
        request.setEndsAt(startsAt);

        assertThrows(InvalidRequestException.class, () -> performanceService.createPerformance(request));
    }

    @Test
    void createPerformance_endsAtEqualToStartsAt_rejected() {
        stubLookups();
        PerformanceRequestDTO request = validRequest();
        request.setEndsAt(startsAt);

        assertThrows(InvalidRequestException.class, () -> performanceService.createPerformance(request));
    }

    @Test
    void createPerformance_adjacentTimes_areAllowed() {
        LocalDateTime adjacentStart = endsAt;
        LocalDateTime adjacentEnd = endsAt.plusHours(1);
        stubLookups();
        when(performanceRepository.existsByStage_IdAndStartsAtLessThanAndEndsAtGreaterThan(10, adjacentEnd, adjacentStart)).thenReturn(false);
        when(performanceRepository.existsByArtist_IdAndStartsAtLessThanAndEndsAtGreaterThan(20, adjacentEnd, adjacentStart)).thenReturn(false);
        when(performanceRepository.save(any(Performance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PerformanceRequestDTO request = validRequest();
        request.setStartsAt(adjacentStart);
        request.setEndsAt(adjacentEnd);

        PerformanceResponseDTO response = performanceService.createPerformance(request);

        assertEquals(ScheduleStatus.SCHEDULED, response.getScheduleStatus());
        verify(performanceRepository).save(any(Performance.class));
    }

    @Test
    void updatePerformance_overlappingStage_usesIdExclusion() {
        stubLookups();
        when(performanceRepository.findById(100)).thenReturn(Optional.of(savedPerformance()));
        when(performanceRepository.existsByStage_IdAndIdNotAndStartsAtLessThanAndEndsAtGreaterThan(10, 100, endsAt, startsAt)).thenReturn(true);

        assertThrows(ScheduleConflictException.class, () -> performanceService.updatePerformance(100, validRequest()));
        verify(performanceRepository, never()).save(any());
    }

    @Test
    void getPerformancesForFestival_missingFestival_throwsNotFound() {
        when(festivalRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> performanceService.getPerformancesForFestival(99));
    }

    private void stubLookups() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(stageRepository.findById(10)).thenReturn(Optional.of(stage));
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));
    }

    private PerformanceRequestDTO validRequest() {
        PerformanceRequestDTO request = new PerformanceRequestDTO();
        request.setFestivalId(1);
        request.setStageId(10);
        request.setArtistId(20);
        request.setStartsAt(startsAt);
        request.setEndsAt(endsAt);
        request.setScheduleStatus(ScheduleStatus.SCHEDULED);
        return request;
    }

    private Performance savedPerformance() {
        Performance performance = new Performance(festival, artist, stage, ScheduleStatus.SCHEDULED, startsAt, endsAt);
        ReflectionTestUtils.setField(performance, "id", 100);
        return performance;
    }
}
