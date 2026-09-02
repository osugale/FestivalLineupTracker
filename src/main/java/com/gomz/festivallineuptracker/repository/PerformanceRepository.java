package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Integer> {
}
