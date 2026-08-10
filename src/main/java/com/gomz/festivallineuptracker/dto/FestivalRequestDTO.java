package com.gomz.festivallineuptracker.dto;

import com.gomz.festivallineuptracker.model.Artist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class FestivalRequestDTO {

    @NotBlank(message = "Festival name cannot be blank")
    private String name;
    @NotBlank(message = "Festival city cannot be blank")
    private String city;
    @NotBlank(message = "Festival country cannot be blank")
    private String country;
    @NotBlank(message = "Festival venue cannot be blank")

    private String venue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageUrl;
    private String officialWebsite;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private String genre;


    public FestivalRequestDTO(){}

    public FestivalRequestDTO(String name, String city,String country,String venue, LocalDate startDate, LocalDate endDate,
                    String description, String imageUrl, String officialWebsite, String genre) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.venue = venue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.imageUrl = imageUrl;
        this.officialWebsite = officialWebsite;
        this.genre = genre;
    }


    public String getName() {return name;}
    public String getCity() {return city;}
    public String getCountry() {return country;}
    public String getVenue() {return venue;}
    public LocalDate getStartDate() {return startDate;}
    public LocalDate getEndDate() {return endDate;}
    public String getDescription() {return description;}
    public String getImageUrl() {return imageUrl;}
    public String getOfficialWebsite() {return officialWebsite;}
    public String getGenre() {return genre;}



    public void setName(String name) {this.name = name;}
    public void setCity(String city) {this.city = city;}
    public void setCountry(String country) {this.country = country;}
    public void setDescription(String description) {this.description = description;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}
    public void setGenre(String genre) {this.genre = genre;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public void setOfficialWebsite(String festivalOfficialWebsite) {this.officialWebsite = festivalOfficialWebsite;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}
    public void setVenue(String venue) {this.venue = venue;}

}
