package com.gomz.festivallineuptracker.repository;
import com.gomz.festivallineuptracker.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Integer> {

    List<Performance> findByFestival_Id(int festivalId);

    List<Performance> findByStage_Id(int stageId);

    List<Performance> findByArtist_Id(int artistId);

    boolean existsByStage_Id(int stageId);

    boolean existsByStage_IdAndStartsAtLessThanAndEndsAtGreaterThan(int stageId, LocalDateTime endsAt, LocalDateTime startsAt);

    boolean existsByStage_IdAndIdNotAndStartsAtLessThanAndEndsAtGreaterThan(int stageId, int id, LocalDateTime endsAt, LocalDateTime startsAt);

    boolean existsByArtist_IdAndStartsAtLessThanAndEndsAtGreaterThan(int artistId, LocalDateTime endsAt, LocalDateTime startsAt);

    boolean existsByArtist_IdAndIdNotAndStartsAtLessThanAndEndsAtGreaterThan(int artistId, int id, LocalDateTime endsAt, LocalDateTime startsAt);
}
