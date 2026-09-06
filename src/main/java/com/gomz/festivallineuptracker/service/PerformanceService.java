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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final FestivalRepository festivalRepository;
    private final StageRepository stageRepository;
    private final ArtistRepository artistRepository;





    public PerformanceService(PerformanceRepository performanceRepository, FestivalRepository festivalRepository,
                              StageRepository stageRepository, ArtistRepository artistRepository) {
        this.performanceRepository = performanceRepository;
        this.festivalRepository = festivalRepository;
        this.stageRepository = stageRepository;
        this.artistRepository = artistRepository;
    }








    public PerformanceResponseDTO createPerformance(PerformanceRequestDTO request) {
        Performance performance = new Performance();
        applyRequest(performance, request, null);
        return toResponse(performanceRepository.save(performance));
    }





    public PerformanceResponseDTO getPerformanceById(int id) {
        return toResponse(findPerformance(id));
    }




    public List<PerformanceResponseDTO> getAllPerformances() {
        return performanceRepository.findAll().stream().map(this::toResponse).toList();
    }





    public PerformanceResponseDTO updatePerformance(int id, PerformanceRequestDTO request) {
        Performance performance = findPerformance(id);
        applyRequest(performance, request, id);
        return toResponse(performanceRepository.save(performance));
    }





    public void deletePerformance(int id) {
        if (!performanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Performance with id " + id + " not found");
        }

        performanceRepository.deleteById(id);
    }





    public List<PerformanceResponseDTO> getPerformancesForFestival(int festivalId) {
        findFestival(festivalId);
        return performanceRepository.findByFestival_Id(festivalId).stream().map(this::toResponse).toList();
    }




    public List<PerformanceResponseDTO> getPerformancesForStage(int stageId) {
        findStage(stageId);
        return performanceRepository.findByStage_Id(stageId).stream().map(this::toResponse).toList();
    }








    public List<PerformanceResponseDTO> getPerformancesForArtist(int artistId) {
        findArtist(artistId);
        return performanceRepository.findByArtist_Id(artistId).stream().map(this::toResponse).toList();
    }









    private void applyRequest(Performance performance, PerformanceRequestDTO request, Integer excludePerformanceId) {
        Festival festival = findFestival(request.getFestivalId());
        Artist artist = findArtist(request.getArtistId());
        Stage stage = findStage(request.getStageId());

        if (stage.getFestival().getId() != festival.getId()) {
            throw new InvalidRequestException("Stage does not belong to the selected festival");
        }

        ScheduleStatus scheduleStatus = request.getScheduleStatus();
        if (scheduleStatus == null) {
            scheduleStatus = ScheduleStatus.TBA;
        }

        LocalDateTime startsAt = request.getStartsAt();
        LocalDateTime endsAt = request.getEndsAt();

        if (scheduleStatus == ScheduleStatus.SCHEDULED) {
            if (startsAt == null || endsAt == null) {
                throw new InvalidRequestException("startsAt and endsAt are required when scheduleStatus is SCHEDULED");
            }
        }

        if (startsAt != null && endsAt != null) {
            if (!endsAt.isAfter(startsAt)) {
                throw new InvalidRequestException("endsAt must be after startsAt");
            }
            assertNoScheduleConflicts(excludePerformanceId, stage.getId(), artist.getId(), startsAt, endsAt);
        }

        performance.setFestival(festival);
        performance.setArtist(artist);
        performance.setStage(stage);
        performance.setScheduleStatus(scheduleStatus);
        performance.setStartsAt(startsAt);
        performance.setEndsAt(endsAt);
    }













    private void assertNoScheduleConflicts(Integer excludePerformanceId, int stageId, int artistId,
                                           LocalDateTime startsAt, LocalDateTime endsAt) {
        boolean stageOverlap;
        boolean artistOverlap;

        if (excludePerformanceId == null) {
            stageOverlap = performanceRepository.existsByStage_IdAndStartsAtLessThanAndEndsAtGreaterThan(stageId, endsAt, startsAt);
            artistOverlap = performanceRepository.existsByArtist_IdAndStartsAtLessThanAndEndsAtGreaterThan(artistId, endsAt, startsAt);
        } else {
            stageOverlap = performanceRepository.existsByStage_IdAndIdNotAndStartsAtLessThanAndEndsAtGreaterThan(
                    stageId, excludePerformanceId, endsAt, startsAt);
            artistOverlap = performanceRepository.existsByArtist_IdAndIdNotAndStartsAtLessThanAndEndsAtGreaterThan(
                    artistId, excludePerformanceId, endsAt, startsAt);
        }

        if (stageOverlap) {
            throw new ScheduleConflictException("Stage already has a performance overlapping this time");
        }

        if (artistOverlap) {
            throw new ScheduleConflictException("Artist already has a performance overlapping this time");
        }
    }













    private Festival findFestival(int festivalId) {
        return festivalRepository.findById(festivalId)
                .orElseThrow(() -> new ResourceNotFoundException("Festival with id " + festivalId + " not found"));
    }

    private Stage findStage(int stageId) {
        return stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage with id " + stageId + " not found"));
    }

    private Artist findArtist(int artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist with id " + artistId + " not found"));
    }

    private Performance findPerformance(int id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance with id " + id + " not found"));
    }










    private PerformanceResponseDTO toResponse(Performance performance) {
        Integer stageId = null;
        String stageName = null;

        if (performance.getStage() != null) {
            stageId = performance.getStage().getId();
            stageName = performance.getStage().getName();
        }

        return new PerformanceResponseDTO(performance.getId(),
                performance.getFestival().getId(),
                performance.getFestival().getName(),
                stageId,
                stageName,
                performance.getArtist().getId(),
                performance.getArtist().getName(),
                performance.getStartsAt(),
                performance.getEndsAt(),
                performance.getScheduleStatus()
        );
    }
}
