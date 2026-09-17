package com.gomz.festivallineuptracker.dto;

public class GenreResponseDTO {

    private int id;
    private String name;
    private String slug;

    public GenreResponseDTO() {
    }

    public GenreResponseDTO(int id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
