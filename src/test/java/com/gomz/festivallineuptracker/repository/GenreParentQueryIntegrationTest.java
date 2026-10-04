package com.gomz.festivallineuptracker.repository;

import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.model.GenreRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class GenreParentQueryIntegrationTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreRelationRepository genreRelationRepository;

    @Test
    void findParentGenres_returnsOnlyTrueParentsUniqueAndSorted() {
        Genre electronic = genreRepository.save(new Genre("Electronic Parent", "electronic-parent-q"));
        Genre hipHop = genreRepository.save(new Genre("Hip Hop Parent", "hip-hop-parent-q"));
        Genre techno = genreRepository.save(new Genre("Techno Child", "techno-child-q"));
        Genre rap = genreRepository.save(new Genre("Rap Child", "rap-child-q"));
        Genre orphan = genreRepository.save(new Genre("Avant-Pop Orphan", "avant-pop-orphan-q"));

        genreRelationRepository.save(new GenreRelation(electronic, techno));
        genreRelationRepository.save(new GenreRelation(electronic, rap));
        genreRelationRepository.save(new GenreRelation(hipHop, rap));

        List<Genre> parents = genreRepository.findParentGenres();
        List<String> names = parents.stream().map(Genre::getName).toList();

        assertTrue(names.contains("Electronic Parent"));
        assertTrue(names.contains("Hip Hop Parent"));
        assertFalse(names.contains("Techno Child"));
        assertFalse(names.contains("Rap Child"));
        assertFalse(names.contains("Avant-Pop Orphan"));
        assertEquals(names.stream().distinct().toList(), names);
        assertEquals(names.stream().sorted(String::compareTo).toList(), names);
        assertEquals(1, names.stream().filter("Electronic Parent"::equals).count());
    }
}
