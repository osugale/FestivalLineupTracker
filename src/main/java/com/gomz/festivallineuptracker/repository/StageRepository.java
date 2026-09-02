package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JpaRepository<Stage, Integer> {
}
