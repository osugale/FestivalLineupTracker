package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.UserArtistFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface UserArtistFavoriteRepository extends JpaRepository<UserArtistFavorite, Integer> {

    List<UserArtistFavorite> findByUserId(int userId);

    @Modifying(clearAutomatically = true)
    void deleteByUserId(int userId);
}
