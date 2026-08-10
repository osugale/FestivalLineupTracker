    package com.gomz.festivallineuptracker.dto;

    import jakarta.persistence.Column;

    public class ArtistResponseDTO {


        private int id;
        private String name;
        private String genre;
        private String country;
        private String imageUrl;
        private String spotifyUrl;
        private String instagramUrl;
        private String soundcloudUrl;
        private String youtubeUrl;
        private String bio;


        public ArtistResponseDTO(){



        }


        public ArtistResponseDTO(int id, String name, String genre, String bio){

            this.id = id;
            this.name = name;
            this.genre = genre;
            this.bio=bio;


        }



        public ArtistResponseDTO (String name,String genre,String country,  String imageUrl, String spotifyUrl, String instagramUrl,
                                 String soundcloudUrl, String youtubeUrl, String bio,int id){
            this.genre=genre;
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

        public ArtistResponseDTO(int id, String name, String genre) {
            this.id = id;
            this.name = name;
            this.genre = genre;
        }


        public int getId() {return id;}
        public String getGenre() {return genre;}
        public String getName() {return name;}
        public String getBio() {return bio;}
        public String getImageUrl() {return imageUrl;}
        public String getCountry() {return country;}
        public String getInstagramUrl() {return instagramUrl;}
        public String getSoundcloudUrl() {return soundcloudUrl;}
        public String getSpotifyUrl() {return spotifyUrl;}
        public String getYoutubeUrl() {return youtubeUrl;}


        public void setId(int id) {this.id = id;}
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
