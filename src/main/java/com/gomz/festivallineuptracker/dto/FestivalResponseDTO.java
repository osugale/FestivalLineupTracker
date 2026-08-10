package com.gomz.festivallineuptracker.dto;

import java.time.LocalDate;

public class FestivalResponseDTO {

    private int id;
    private String name;
    private String city;
    private String country;
    private String venue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String imageUrl;
    private String officialWebsite;
    private String genre;




    public FestivalResponseDTO() {
    }

    public FestivalResponseDTO(int id,String name, String city) {
        this.id=id;
        this.name=name;
        this.city=city;


    }

    public FestivalResponseDTO(int id,String name, String city,String country,String venue, LocalDate startDate, LocalDate endDate,
                              String description, String imageUrl, String officialWebsite, String genre) {
        this.id=id;
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


    public int getId() {return id;}
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


    public void setId(int id) {this.id = id;}
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
