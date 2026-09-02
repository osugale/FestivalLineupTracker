package com.gomz.festivallineuptracker.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "performance")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_status", nullable = false, length = 20)
    private ScheduleStatus scheduleStatus = ScheduleStatus.TBA;

    @Column(name = "starts_at")
    private LocalDateTime startsAt;

    @Column(name = "ends_at")
    private LocalDateTime endsAt;

    public Performance() {
    }

    public Performance(Festival festival, Artist artist, Stage stage, ScheduleStatus scheduleStatus,
                       LocalDateTime startsAt, LocalDateTime endsAt) {
        this.festival = festival;
        this.artist = artist;
        this.stage = stage;
        this.scheduleStatus = scheduleStatus;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public int getId() {
        return id;
    }

    public Festival getFestival() {
        return festival;
    }

    public Artist getArtist() {
        return artist;
    }

    public Stage getStage() {
        return stage;
    }

    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }
}
