package com.gomz.festivallineuptracker.dto;

import com.gomz.festivallineuptracker.model.Festival;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ArtistRequestDTO {

    @NotBlank(message = "Artist name cannot be blank")
    private String name;
    @NotBlank(message = "Artist genre cannot be blank")
    private String genre;
    @NotBlank(message = "Artist Country cannot be blank")
    private String country;

    private String imageUrl;
    private String spotifyUrl;
    private String instagramUrl;
    private String soundcloudUrl;
    private String youtubeUrl;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;


    public ArtistRequestDTO(){


    }



    public ArtistRequestDTO (String name,String genre,String country,  String imageUrl, String spotifyUrl, String instagramUrl,
                  String soundcloudUrl, String youtubeUrl, String bio){
        this.genre=genre;
        this.name=name;
        this.country=country;
        this.imageUrl = imageUrl;
        this.spotifyUrl = spotifyUrl;
        this.instagramUrl = instagramUrl;
        this.soundcloudUrl = soundcloudUrl;
        this.youtubeUrl = youtubeUrl;
        this.bio = bio;

    }


    public String getGenre() {return genre;}
    public String getName() {return name;}
    public String getBio() {return bio;}
    public String getImageUrl() {return imageUrl;}
    public String getCountry() {return country;}
    public String getInstagramUrl() {return instagramUrl;}
    public String getSoundcloudUrl() {return soundcloudUrl;}
    public String getSpotifyUrl() {return spotifyUrl;}
    public String getYoutubeUrl() {return youtubeUrl;}




    public void setName(String name) {this.name = name;}
    public void setGenre(String genre) {this.genre = genre;}
    public void setBio(String bio) {this.bio = bio;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public void setInstagramUrl(String instagramUrl) {this.instagramUrl = instagramUrl;}
    public void setSoundcloudUrl(String soundcloudUrl) {this.soundcloudUrl = soundcloudUrl;}
    public void setSpotifyUrl(String spotifyUrl) {this.spotifyUrl = spotifyUrl;}
    public void setYoutubeUrl(String youtubeUrl) {this.youtubeUrl = youtubeUrl;}
    public void setCountry(String country) {this.country = country;}
}
