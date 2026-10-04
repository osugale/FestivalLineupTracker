package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.exception.ResourceInUseException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.model.Performance;
import com.gomz.festivallineuptracker.model.ScheduleStatus;
import com.gomz.festivallineuptracker.model.Stage;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import com.gomz.festivallineuptracker.repository.PerformanceRepository;
import com.gomz.festivallineuptracker.repository.StageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CatalogDeleteAndPagingIntegrationTest {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private FestivalService festivalService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private StageService stageService;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private StageRepository stageRepository;

    @Test
    void getArtists_pageSizeIsNotInflatedByGenreCollections() {
        Genre electronic = savedGenre("Electronic-N1", "electronic-n1");
        Artist alpha = savedArtist("Alpha N1", "alpha-n1", electronic);
        savedArtist("Beta N1", "beta-n1", electronic);
        savedArtist("Gamma N1", "gamma-n1", electronic);

        Page<?> page = artistService.getArtists(0, 2, "name");

        assertEquals(2, page.getContent().size());
        assertTrue(page.getTotalElements() >= 3);
        assertEquals("Alpha N1", artistRepository.findById(alpha.getId()).orElseThrow().getName());
    }

    @Test
    void deleteUnreferencedArtistAndGenre_succeeds() {
        Genre genre = savedGenre("Orphan Genre", "orphan-genre");
        Artist artist = savedArtist("Orphan Artist", "orphan-artist", genre);

        assertTrue(artistService.deleteArtist(artist.getId()));
        assertFalse(artistRepository.existsById(artist.getId()));

        genreService.deleteGenre(genre.getId());
        assertFalse(genreRepository.existsById(genre.getId()));
    }

    @Test
    void deleteArtistOnLineup_returnsConflictAndKeepsLineup() {
        Genre genre = savedGenre("Lineup Genre", "lineup-genre");
        Artist artist = savedArtist("Lineup Artist", "lineup-artist", genre);
        Festival festival = savedFestival("Lineup Fest", LocalDate.of(2026, 6, 1));
        festival.getArtists().add(artist);
        festivalRepository.save(festival);

        assertThrows(ResourceInUseException.class, () -> artistService.deleteArtist(artist.getId()));
        assertTrue(artistRepository.existsById(artist.getId()));
        assertTrue(festivalRepository.existsByArtists_Id(artist.getId()));
    }

    @Test
    void deleteArtistWithPerformance_returnsConflictAndKeepsPerformance() {
        Genre genre = savedGenre("Perf Genre", "perf-genre");
        Artist artist = savedArtist("Perf Artist", "perf-artist", genre);
        Festival festival = savedFestival("Perf Fest", LocalDate.of(2026, 6, 2));
        Performance performance = new Performance(festival, artist, null, ScheduleStatus.TBA, null, null);
        performanceRepository.save(performance);

        assertThrows(ResourceInUseException.class, () -> artistService.deleteArtist(artist.getId()));
        assertTrue(performanceRepository.existsById(performance.getId()));
        assertTrue(artistRepository.existsById(artist.getId()));
    }

    @Test
    void deleteStageWithPerformance_returnsConflictAndKeepsPerformance() {
        Festival festival = savedFestival("Stage Fest", LocalDate.of(2026, 6, 3));
        Stage stage = new Stage(festival, "Main");
        stageRepository.save(stage);
        Genre genre = savedGenre("Stage Genre", "stage-genre");
        Artist artist = savedArtist("Stage Artist", "stage-artist", genre);
        Performance performance = new Performance(festival, artist, stage, ScheduleStatus.TBA, null, null);
        performanceRepository.save(performance);

        assertThrows(ResourceInUseException.class, () -> stageService.deleteStage(stage.getId()));
        assertTrue(stageRepository.existsById(stage.getId()));
        assertTrue(performanceRepository.existsById(performance.getId()));
    }

    @Test
    void deleteFestivalWithStage_returnsConflictAndKeepsStage() {
        Festival festival = savedFestival("Kept Fest", LocalDate.of(2026, 6, 4));
        Stage stage = new Stage(festival, "Tent");
        stageRepository.save(stage);

        assertThrows(ResourceInUseException.class, () -> festivalService.deleteFestival(festival.getId()));
        assertTrue(festivalRepository.existsById(festival.getId()));
        assertTrue(stageRepository.existsById(stage.getId()));
    }

    @Test
    void deleteUnreferencedFestival_succeeds() {
        Festival festival = savedFestival("Free Fest", LocalDate.of(2026, 6, 5));

        assertTrue(festivalService.deleteFestival(festival.getId()));
        assertFalse(festivalRepository.existsById(festival.getId()));
    }

    private Genre savedGenre(String name, String slug) {
        return genreRepository.save(new Genre(name, slug));
    }

    private Artist savedArtist(String name, String slug, Genre genre) {
        Artist artist = new Artist();
        artist.setName(name);
        artist.setSlug(slug);
        artist.setCountry("UK");
        artist.setGenres(Set.of(genre));
        return artistRepository.save(artist);
    }

    private Festival savedFestival(String name, LocalDate startDate) {
        Festival festival = new Festival();
        festival.setName(name);
        festival.setCity("Lisbon");
        festival.setCountry("Portugal");
        festival.setVenue("Park");
        festival.setStartDate(startDate);
        festival.setEndDate(startDate.plusDays(1));
        festival.setTimezone("Europe/Lisbon");
        return festivalRepository.save(festival);
    }
}
