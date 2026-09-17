package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.GenreRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRelationRepository extends JpaRepository<GenreRelation, Integer> {

    boolean existsByParentGenre_IdAndChildGenre_Id(int parentGenreId, int childGenreId);

    List<GenreRelation> findByParentGenre_Id(int parentGenreId);

    List<GenreRelation> findByChildGenre_Id(int childGenreId);

    void deleteByParentGenre_Id(int parentGenreId);

    void deleteByChildGenre_Id(int childGenreId);
}
