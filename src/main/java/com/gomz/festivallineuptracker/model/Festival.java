package com.gomz.festivallineuptracker.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_festival_name_start_date", columnNames = {"name", "start_date"})})
public class Festival {




    @ManyToMany
    @JoinTable(name = "festival_artist", joinColumns = @JoinColumn(name = "festival_id"), inverseJoinColumns = @JoinColumn(name = "artist_id"),
            uniqueConstraints = @UniqueConstraint(name = "uk_festival_artist_pair", columnNames = {"festival_id", "artist_id"}),
            indexes = @Index(name = "idx_festival_artist_artist_festival", columnList = "artist_id, festival_id"))
    private Set<Artist> artists = new HashSet<>();






    @ManyToMany
    @JoinTable(name = "festival_genre", joinColumns = @JoinColumn(name = "festival_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new LinkedHashSet<>();





    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


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

    @Column(nullable = false, length = 80)
    private String timezone;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String officialWebsite;



    public Festival(){}

    public Festival(String name, String city,String country,String venue, LocalDate startDate, LocalDate endDate,
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

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists == null ? new HashSet<>() : artists;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }


    public int getId() {return id;}
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



    public void setName(String name) {this.name = name;}
    public void setCity(String city) {this.city = city;}
    public void setCountry(String country) {this.country = country;}
    public void setDescription(String description) {this.description = description;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}
    public void setTimezone(String timezone) {this.timezone = timezone;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public void setOfficialWebsite(String festivalOfficialWebsite) {this.officialWebsite = festivalOfficialWebsite;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}
    public void setVenue(String venue) {this.venue = venue;}

}
