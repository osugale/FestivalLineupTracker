package com.gomz.festivallineuptracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_artist_favorite", uniqueConstraints = {@UniqueConstraint(name = "uk_user_artist_favorite", columnNames = {"user_id", "artist_id"})})
public class UserArtistFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "artist_id", nullable = false)
    private int artistId;

    public UserArtistFavorite() {
    }

    public UserArtistFavorite(int userId, int artistId) {
        this.userId = userId;
        this.artistId = artistId;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }
}
