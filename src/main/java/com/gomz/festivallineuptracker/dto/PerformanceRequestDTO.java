package com.gomz.festivallineuptracker.dto;

import com.gomz.festivallineuptracker.model.ScheduleStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class PerformanceRequestDTO {

    @NotNull(message = "Festival id is required")
    @Min(value = 1, message = "Festival id must be a positive number")
    private Integer festivalId;

    @NotNull(message = "Artist id is required")
    @Min(value = 1, message = "Artist id must be a positive number")
    private Integer artistId;

    @Min(value = 1, message = "Stage id must be a positive number")
    private Integer stageId;

    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private ScheduleStatus scheduleStatus;




    public PerformanceRequestDTO() {
    }




    public Integer getFestivalId() {
        return festivalId;
    }

    public Integer getArtistId() {
        return artistId;
    }

    public Integer getStageId() {
        return stageId;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    public void setFestivalId(Integer festivalId) {
        this.festivalId = festivalId;
    }

    public void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public void setScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }
}
