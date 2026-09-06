package com.gomz.festivallineuptracker.dto;

import com.gomz.festivallineuptracker.model.ScheduleStatus;

import java.time.LocalDateTime;

public class PerformanceResponseDTO {

    private int id;
    private int festivalId;
    private String festivalName;
    private Integer stageId;
    private String stageName;
    private int artistId;
    private String artistName;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private ScheduleStatus scheduleStatus;

    public PerformanceResponseDTO() {
    }

    public PerformanceResponseDTO(int id, int festivalId, String festivalName, Integer stageId, String stageName,
                                  int artistId, String artistName, LocalDateTime startsAt, LocalDateTime endsAt,
                                  ScheduleStatus scheduleStatus) {
        this.id = id;
        this.festivalId = festivalId;
        this.festivalName = festivalName;
        this.stageId = stageId;
        this.stageName = stageName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.scheduleStatus = scheduleStatus;
    }

    public int getId() {
        return id;
    }

    public int getFestivalId() {
        return festivalId;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public Integer getStageId() {
        return stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public int getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setFestivalId(int festivalId) {
        this.festivalId = festivalId;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
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
