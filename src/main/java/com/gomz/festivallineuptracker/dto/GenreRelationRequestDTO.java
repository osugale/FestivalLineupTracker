package com.gomz.festivallineuptracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class GenreRelationRequestDTO {

    @NotNull(message = "Child genre id is required")
    @Min(value = 1, message = "Child genre id must be a positive number")
    private Integer childGenreId;

    public GenreRelationRequestDTO() {
    }




    public Integer getChildGenreId() {
        return childGenreId;
    }




    public void setChildGenreId(Integer childGenreId) {
        this.childGenreId = childGenreId;
    }
}
