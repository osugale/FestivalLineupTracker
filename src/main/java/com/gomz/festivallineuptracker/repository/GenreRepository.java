package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, int id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, int id);

    Optional<Genre> findBySlug(String slug);
}
