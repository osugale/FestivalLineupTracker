package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.Artist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    @EntityGraph(attributePaths = "genres")
    List<Artist> findByNameContaining(String name);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, int id);

    boolean existsByGenres_Id(int genreId);

    @EntityGraph(attributePaths = "genres")
    List<Artist> findWithGenresByIdIn(Collection<Integer> ids);

    @EntityGraph(attributePaths = "genres")
    @Query("select distinct a from Artist a join a.genres g where g.id in :genreIds")
    List<Artist> findDistinctByGenreIds(@Param("genreIds") Collection<Integer> genreIds);

    @EntityGraph(attributePaths = "festivals.genres")
    @Query("select a from Artist a where a.id = :id")
    Optional<Artist> findWithFestivalsById(@Param("id") int id);
}
