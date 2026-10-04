package com.gomz.festivallineuptracker.dto;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GenrePreferenceRequestDTO {

    @NotNull(message = "genreIds is required")
    private List<Integer> genreIds = new ArrayList<>();

    public GenrePreferenceRequestDTO() {
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds == null ? new ArrayList<>() : genreIds;
    }
}
