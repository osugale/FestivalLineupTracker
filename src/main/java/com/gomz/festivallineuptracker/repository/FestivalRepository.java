package com.gomz.festivallineuptracker.repository;


import com.gomz.festivallineuptracker.model.Festival;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FestivalRepository extends JpaRepository<Festival, Integer> {

    @EntityGraph(attributePaths = "genres")
    List<Festival> findByNameContaining(String name);

    boolean existsByNameAndStartDate(String name, LocalDate startDate);

    boolean existsByNameAndStartDateAndIdNot(String name, LocalDate startDate, int id);

    boolean existsByGenres_Id(int genreId);

    boolean existsByArtists_Id(int artistId);

    boolean existsByIdAndArtistsIsNotEmpty(int id);

    @Query("select f.id, g.id from Festival f join f.genres g")
    List<Object[]> findFestivalGenreIds();

    @Query("select distinct f.id from Festival f join f.artists a where a.id in :artistIds")
    List<Integer> findFestivalIdsByArtistIds(@Param("artistIds") Collection<Integer> artistIds);

    @EntityGraph(attributePaths = "genres")
    List<Festival> findWithGenresByIdIn(Collection<Integer> ids);

    @EntityGraph(attributePaths = "artists.genres")
    @Query("select f from Festival f where f.id = :id")
    Optional<Festival> findWithArtistsById(@Param("id") int id);
}
