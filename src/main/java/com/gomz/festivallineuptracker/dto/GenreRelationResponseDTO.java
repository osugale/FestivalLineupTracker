package com.gomz.festivallineuptracker.dto;

public class GenreRelationResponseDTO {

    private int id;
    private GenreResponseDTO parentGenre;
    private GenreResponseDTO childGenre;

    public GenreRelationResponseDTO() {
    }

    public GenreRelationResponseDTO(int id, GenreResponseDTO parentGenre, GenreResponseDTO childGenre) {
        this.id = id;
        this.parentGenre = parentGenre;
        this.childGenre = childGenre;
    }

    public int getId() {
        return id;
    }

    public GenreResponseDTO getParentGenre() {
        return parentGenre;
    }

    public GenreResponseDTO getChildGenre() {
        return childGenre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParentGenre(GenreResponseDTO parentGenre) {
        this.parentGenre = parentGenre;
    }

    public void setChildGenre(GenreResponseDTO childGenre) {
        this.childGenre = childGenre;
    }
}
