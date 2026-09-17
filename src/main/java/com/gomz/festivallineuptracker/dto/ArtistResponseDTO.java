package com.gomz.festivallineuptracker.dto;

import java.util.ArrayList;
import java.util.List;

public class ArtistResponseDTO {


        private int id;
        private String name;
        private String slug;
        private String country;
        private String imageUrl;
        private String spotifyUrl;
        private String instagramUrl;
        private String soundcloudUrl;
        private String youtubeUrl;
        private String bio;
        private List<GenreResponseDTO> genres = new ArrayList<>();


        public ArtistResponseDTO(){



        }


        public ArtistResponseDTO (int id, String name, String slug, String country,  String imageUrl, String spotifyUrl, String instagramUrl,
                                 String soundcloudUrl, String youtubeUrl, String bio, List<GenreResponseDTO> genres){
            this.id=id;
            this.name=name;
            this.slug=slug;
            this.country=country;
            this.imageUrl = imageUrl;
            this.spotifyUrl = spotifyUrl;
            this.instagramUrl = instagramUrl;
            this.soundcloudUrl = soundcloudUrl;
            this.youtubeUrl = youtubeUrl;
            this.bio = bio;
            this.genres = genres == null ? new ArrayList<>() : genres;

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
        public List<GenreResponseDTO> getGenres() {return genres;}


        public void setId(int id) {this.id = id;}
        public void setName(String name) {this.name = name;}
        public void setSlug(String slug) {this.slug = slug;}
        public void setBio(String bio) {this.bio = bio;}
        public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
        public void setInstagramUrl(String instagramUrl) {this.instagramUrl = instagramUrl;}
        public void setSoundcloudUrl(String soundcloudUrl) {this.soundcloudUrl = soundcloudUrl;}
        public void setSpotifyUrl(String spotifyUrl) {this.spotifyUrl = spotifyUrl;}
        public void setYoutubeUrl(String youtubeUrl) {this.youtubeUrl = youtubeUrl;}
        public void setCountry(String country) {this.country = country;}
        public void setGenres(List<GenreResponseDTO> genres) {
            this.genres = genres == null ? new ArrayList<>() : genres;
        }
}
