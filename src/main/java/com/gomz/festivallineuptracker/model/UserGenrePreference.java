package com.gomz.festivallineuptracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_genre_preference", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_genre_preference", columnNames = {"user_id", "genre_id"})
})
public class UserGenrePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "genre_id", nullable = false)
    private int genreId;

    public UserGenrePreference() {
    }

    public UserGenrePreference(int userId, int genreId) {
        this.userId = userId;
        this.genreId = genreId;
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

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
}
