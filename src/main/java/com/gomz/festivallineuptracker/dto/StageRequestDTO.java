package com.gomz.festivallineuptracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StageRequestDTO {

    @NotNull(message = "Festival id required")
    @Min(value = 1, message = "Festival id should be a positive number")
    private Integer festivalId;

    @NotBlank(message = "Stage name cannot be blank")
    @Size(max = 150, message = "Stage name cannot exceed 150 characters")
    private String name;

    public StageRequestDTO() {

    }



    public Integer getFestivalId() {
        return festivalId;
    }

    public String getName() {
        return name;
    }

    public void setFestivalId(Integer festivalId) {
        this.festivalId = festivalId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
