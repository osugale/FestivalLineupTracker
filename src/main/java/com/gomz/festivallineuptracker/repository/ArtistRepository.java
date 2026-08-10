package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArtistRepository extends JpaRepository<Artist,Integer> {

    List<Artist> findByNameContaining(String name);




}


