package com.gomz.festivallineuptracker.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @NotNull(message = "Festival start date is required")
    private LocalDate startDate;

    @NotNull(message = "Festival end date is required")
    private LocalDate endDate;

    @NotBlank(message = "Festival timezone cannot be blank")
    @Size(max = 80, message = "Festival timezone cannot exceed 80 characters")
    private String timezone;

    private String imageUrl;
    private String officialWebsite;
    private String timetableUrl;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private List<Integer> genreIds = new ArrayList<>();


    public FestivalRequestDTO(){}

    public FestivalRequestDTO(String name, String city,String country,String venue, LocalDate startDate, LocalDate endDate,
                    String description, String imageUrl, String officialWebsite, String timezone) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.venue = venue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.imageUrl = imageUrl;
        this.officialWebsite = officialWebsite;
        this.timezone = timezone;
    }


    public String getName() {return name;}
    public String getCity() {return city;}
    public String getCountry() {return country;}
    public String getVenue() {return venue;}
    public LocalDate getStartDate() {return startDate;}
    public LocalDate getEndDate() {return endDate;}
    public String getTimezone() {return timezone;}
    public String getDescription() {return description;}
    public String getImageUrl() {return imageUrl;}
    public String getOfficialWebsite() {return officialWebsite;}
    public String getTimetableUrl() {return timetableUrl;}
    public List<Integer> getGenreIds() {return genreIds;}

    @AssertTrue(message = "endDate must be on or after startDate")
    public boolean isEndDateOnOrAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }


    public void setName(String name) {this.name = name;}
    public void setCity(String city) {this.city = city;}
    public void setCountry(String country) {this.country = country;}
    public void setDescription(String description) {this.description = description;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}
    public void setTimezone(String timezone) {this.timezone = timezone;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public void setOfficialWebsite(String festivalOfficialWebsite) {this.officialWebsite = festivalOfficialWebsite;}
    public void setTimetableUrl(String timetableUrl) {this.timetableUrl = timetableUrl;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}
    public void setVenue(String venue) {this.venue = venue;}
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds == null ? new ArrayList<>() : genreIds;
    }

}
