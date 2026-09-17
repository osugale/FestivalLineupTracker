package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.StageRequestDTO;
import com.gomz.festivallineuptracker.dto.StageResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.model.Stage;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.PerformanceRepository;
import com.gomz.festivallineuptracker.repository.StageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageService {

    private final StageRepository stageRepository;
    private final FestivalRepository festivalRepository;
    private final PerformanceRepository performanceRepository;




    public StageService(StageRepository stageRepository, FestivalRepository festivalRepository,
                        PerformanceRepository performanceRepository) {
        this.stageRepository = stageRepository;
        this.festivalRepository = festivalRepository;
        this.performanceRepository = performanceRepository;
    }

    private StageResponseDTO toResponse(Stage stage) {
        return new StageResponseDTO(stage.getId(), stage.getFestival().getId(), stage.getFestival().getName(), stage.getName());
    }


    private Festival findFestival(int festivalId) {
        return festivalRepository.findById(festivalId)
                .orElseThrow(() -> new ResourceNotFoundException("Festival with id " + festivalId + " not found"));
    }



    private Stage findStage(int id) {
        return stageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage with id " + id + " not found"));
    }








    public StageResponseDTO createStage(StageRequestDTO request) {
        Festival festival = findFestival(request.getFestivalId());

        if (stageRepository.existsByFestival_IdAndName(festival.getId(), request.getName())) {
            throw new DuplicateResourceException("Stage with name '" + request.getName() + "' already exists for this festival");
        }

        Stage stage = new Stage();
        stage.setFestival(festival);
        stage.setName(request.getName());

        return toResponse(stageRepository.save(stage)) ;

    }






    public StageResponseDTO getStageById(int id) {
        return toResponse(findStage(id));
    }




    public List<StageResponseDTO> getAllStages() {
        return stageRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<StageResponseDTO> getStagesForFestival(int festivalId) {
        findFestival(festivalId);
        return stageRepository.findByFestival_IdOrderByNameAsc(festivalId).stream().map(this::toResponse).toList();
    }







    public StageResponseDTO updateStage(int id, StageRequestDTO request) {
        Stage stage = findStage(id);
        Festival festival = findFestival(request.getFestivalId());

        if (stage.getFestival().getId() != festival.getId()
                && performanceRepository.existsByStage_Id(stage.getId())) {
            throw new InvalidRequestException("A stage with performances cannot be moved to another festival");
        }

        if (stageRepository.existsByFestival_IdAndNameAndIdNot(festival.getId(), request.getName(), id)) {
            throw new DuplicateResourceException("Stage with name '" + request.getName() + "' already exists for this festival");
        }

        stage.setFestival(festival);
        stage.setName(request.getName());

        return toResponse(stageRepository.save(stage));
    }







    public void deleteStage(int id) {
        if (!stageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stage with id " + id + " not found");
        }

        stageRepository.deleteById(id);
    }














}
