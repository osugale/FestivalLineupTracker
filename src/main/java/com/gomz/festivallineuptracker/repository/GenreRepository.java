package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, int id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, int id);

    Optional<Genre> findBySlug(String slug);

    @Query("select g from Genre g where g.id not in (select r.childGenre.id from GenreRelation r)")
    List<Genre> findRootGenres();

    @Query("""
            select distinct parent from GenreRelation relation
            join relation.parentGenre parent
            where parent.id not in (select childRelation.childGenre.id from GenreRelation childRelation)
            order by parent.name
            """)
    List<Genre> findParentGenres();
}
