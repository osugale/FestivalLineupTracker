package com.gomz.festivallineuptracker.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToMany
    @JoinTable(name="festival_artist", joinColumns=@JoinColumn(name="festival_id"), inverseJoinColumns=@JoinColumn(name = "artist_id"))
    private List<Artist> artists = new ArrayList<>();

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 150)
    private String venue;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String officialWebsite;

    @Column(length = 100)
    private String genre;


    public Festival(){}

    public Festival(String name, String city,String country,String venue, LocalDate startDate, LocalDate endDate,
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

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
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
