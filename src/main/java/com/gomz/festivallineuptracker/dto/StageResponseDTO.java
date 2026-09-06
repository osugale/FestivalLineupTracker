package com.gomz.festivallineuptracker.dto;

import com.gomz.festivallineuptracker.model.Festival;

public class StageResponseDTO {

    private int id;
    private int festivalId;
    private String festivalName;
    private String name;

    public StageResponseDTO() {
    }

    public StageResponseDTO(int id, int festivalId, String festivalName, String name) {
        this.id = id;
        this.festivalId = festivalId;
        this.festivalName = festivalName;
        this.name = name;
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

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }
}
