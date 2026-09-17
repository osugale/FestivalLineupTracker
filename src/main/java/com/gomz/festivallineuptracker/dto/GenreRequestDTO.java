package com.gomz.festivallineuptracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GenreRequestDTO {

    @NotBlank(message = "Genre name cannot be blank")
    @Size(max = 150, message = "Genre name cannot exceed 150 characters")
    private String name;

    public GenreRequestDTO() {
    }

    public GenreRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
