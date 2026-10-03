package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.UserGenrePreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface UserGenrePreferenceRepository extends JpaRepository<UserGenrePreference, Integer> {

    List<UserGenrePreference> findByUserId(int userId);

    @Modifying(clearAutomatically = true)
    void deleteByUserId(int userId);
}
