package com.gomz.festivallineuptracker.dto;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FavoriteArtistsRequestDTO {

    @NotNull(message = "artistIds is required")
    private List<Integer> artistIds = new ArrayList<>();




    public FavoriteArtistsRequestDTO() {
    }

    public List<Integer> getArtistIds() {
        return artistIds;
    }




    public void setArtistIds(List<Integer> artistIds) {
        this.artistIds = artistIds == null ? new ArrayList<>() : artistIds;
    }
}
