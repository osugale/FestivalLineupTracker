package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JpaRepository<Stage, Integer> {

    boolean existsByFestival_IdAndName(int festivalId, String name);

    boolean existsByFestival_IdAndNameAndIdNot(int festivalId, String name, int id);
}
