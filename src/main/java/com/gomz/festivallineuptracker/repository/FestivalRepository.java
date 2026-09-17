package com.gomz.festivallineuptracker.repository;


import com.gomz.festivallineuptracker.model.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FestivalRepository extends JpaRepository<Festival, Integer> {

    List<Festival> findByNameContaining(String name);

    boolean existsByNameAndStartDate(String name, LocalDate startDate);

    boolean existsByNameAndStartDateAndIdNot(String name, LocalDate startDate, int id);

}
