package com.gomz.festivallineuptracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_artist_slug", columnNames = "slug")})
public class Artist {

    public Artist(){

    }

    @JsonIgnore
    public List<Festival> getFestivals() {
        return festivals;
    }


    @ManyToMany(mappedBy="artists")
    private List<Festival> festivals;



    @ManyToMany
    @JoinTable(name = "artist_genre", joinColumns = @JoinColumn(name = "artist_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new LinkedHashSet<>();




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 180, unique = true)
    private String slug;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String spotifyUrl;

    @Column(length = 500)
    private String instagramUrl;

    @Column(length = 500)
    private String soundcloudUrl;

    @Column(length = 500)
    private String youtubeUrl;



    @Column(length = 1000)
    private String bio;


    public Artist(String name, String country,  String imageUrl, String spotifyUrl, String instagramUrl, String soundcloudUrl, String youtubeUrl, String bio,int id)
    {
        this.name=name;
        this.country=country;
        this.imageUrl = imageUrl;
        this.spotifyUrl = spotifyUrl;
        this.instagramUrl = instagramUrl;
        this.soundcloudUrl = soundcloudUrl;
        this.youtubeUrl = youtubeUrl;
        this.bio = bio;
        this.id=id;

    }


    public int getId() {return id;}
    public String getName() {return name;}
    public String getSlug() {return slug;}
    public String getBio() {return bio;}
    public String getImageUrl() {return imageUrl;}
    public String getCountry() {return country;}
    public String getInstagramUrl() {return instagramUrl;}
    public String getSoundcloudUrl() {return soundcloudUrl;}
    public String getSpotifyUrl() {return spotifyUrl;}
    public String getYoutubeUrl() {return youtubeUrl;}
    public Set<Genre> getGenres() {return genres;}




    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setSlug(String slug) {this.slug = slug;}
    public void setBio(String bio) {this.bio = bio;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public void setInstagramUrl(String instagramUrl) {this.instagramUrl = instagramUrl;}
    public void setSoundcloudUrl(String soundcloudUrl) {this.soundcloudUrl = soundcloudUrl;}
    public void setSpotifyUrl(String spotifyUrl) {this.spotifyUrl = spotifyUrl;}
    public void setYoutubeUrl(String youtubeUrl) {this.youtubeUrl = youtubeUrl;}
    public void setFestivals(List<Festival> festivals) {this.festivals = festivals;}
    public void setCountry(String country) {this.country = country;}
    public void setGenres(Set<Genre> genres) {this.genres = genres;}
}
