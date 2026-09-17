package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.GenreRelationResponseDTO;
import com.gomz.festivallineuptracker.dto.GenreRequestDTO;
import com.gomz.festivallineuptracker.dto.GenreResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.model.GenreRelation;
import com.gomz.festivallineuptracker.repository.GenreRelationRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock private GenreRepository genreRepository;
    @Mock private GenreRelationRepository genreRelationRepository;
    @InjectMocks private GenreService genreService;

    private Genre drumAndBass;
    private Genre jungle;
    private Genre liquidDrumAndBass;

    @BeforeEach
    void setUp() {
        drumAndBass = genre(1, "Drum & Bass", "drum-and-bass");
        jungle = genre(2, "Jungle", "jungle");
        liquidDrumAndBass = genre(3, "Liquid Drum & Bass", "liquid-drum-and-bass");
    }

    @Test
    void createGenre_savesNormalizedSlug() {
        when(genreRepository.existsByNameIgnoreCase("Drum & Bass")).thenReturn(false);
        when(genreRepository.existsBySlug("drum-and-bass")).thenReturn(false);
        when(genreRepository.save(any(Genre.class))).thenAnswer(invocation -> {
            Genre saved = invocation.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 1);
            return saved;
        });

        GenreResponseDTO result = genreService.createGenre(new GenreRequestDTO("Drum & Bass"));

        assertEquals("drum-and-bass", result.getSlug());
    }

    @Test
    void createGenre_duplicateName_rejected() {
        when(genreRepository.existsByNameIgnoreCase("Techno")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> genreService.createGenre(new GenreRequestDTO("Techno")));
    }

    @Test
    void createGenre_duplicateSlug_rejected() {
        when(genreRepository.existsByNameIgnoreCase("Drum and Bass")).thenReturn(false);
        when(genreRepository.existsBySlug("drum-and-bass")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> genreService.createGenre(new GenreRequestDTO("Drum and Bass")));
    }

    @Test
    void updateGenre_updatesNameAndSlug() {
        when(genreRepository.findById(1)).thenReturn(Optional.of(drumAndBass));
        when(genreRepository.existsByNameIgnoreCaseAndIdNot("Bass Music", 1)).thenReturn(false);
        when(genreRepository.existsBySlugAndIdNot("bass-music", 1)).thenReturn(false);
        when(genreRepository.save(drumAndBass)).thenReturn(drumAndBass);

        GenreResponseDTO result = genreService.updateGenre(1, new GenreRequestDTO("Bass Music"));

        assertEquals("Bass Music", result.getName());
        assertEquals("bass-music", result.getSlug());
    }

    @Test
    void createRelation_storesParentToChildTaxonomyWithoutAffinity() {
        stubGenres(1, drumAndBass, 2, jungle);
        when(genreRelationRepository.existsByParentGenre_IdAndChildGenre_Id(1, 2)).thenReturn(false);
        when(genreRelationRepository.findByParentGenre_Id(2)).thenReturn(List.of());
        when(genreRelationRepository.save(any(GenreRelation.class))).thenAnswer(invocation -> {
            GenreRelation saved = invocation.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 9);
            return saved;
        });

        GenreRelationResponseDTO result = genreService.createRelation(1, 2);

        assertEquals(9, result.getId());
        assertEquals("Drum & Bass", result.getParentGenre().getName());
        assertEquals("Jungle", result.getChildGenre().getName());
    }

    @Test
    void createRelation_selfRelation_rejected() {
        when(genreRepository.findById(1)).thenReturn(Optional.of(drumAndBass));

        assertThrows(InvalidRequestException.class, () -> genreService.createRelation(1, 1));
    }

    @Test
    void createRelation_duplicatePair_rejected() {
        stubGenres(1, drumAndBass, 2, jungle);
        when(genreRelationRepository.existsByParentGenre_IdAndChildGenre_Id(1, 2)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> genreService.createRelation(1, 2));
    }

    @Test
    void createRelation_reverseEdgeThatCreatesCycle_rejected() {
        stubGenres(2, jungle, 1, drumAndBass);
        when(genreRelationRepository.existsByParentGenre_IdAndChildGenre_Id(2, 1)).thenReturn(false);
        when(genreRelationRepository.findByParentGenre_Id(1)).thenReturn(List.of(new GenreRelation(drumAndBass, jungle)));

        assertThrows(InvalidRequestException.class, () -> genreService.createRelation(2, 1));
    }

    @Test
    void createRelation_longerCycle_rejected() {
        stubGenres(3, liquidDrumAndBass, 1, drumAndBass);
        when(genreRelationRepository.existsByParentGenre_IdAndChildGenre_Id(3, 1)).thenReturn(false);
        when(genreRelationRepository.findByParentGenre_Id(1)).thenReturn(List.of(new GenreRelation(drumAndBass, jungle)));
        when(genreRelationRepository.findByParentGenre_Id(2)).thenReturn(List.of(new GenreRelation(jungle, liquidDrumAndBass)));

        assertThrows(InvalidRequestException.class, () -> genreService.createRelation(3, 1));
    }

    @Test
    void getRelationsForGenre_returnsChildren() {
        when(genreRepository.findById(1)).thenReturn(Optional.of(drumAndBass));
        when(genreRelationRepository.findByParentGenre_Id(1)).thenReturn(List.of(new GenreRelation(drumAndBass, jungle)));

        List<GenreRelationResponseDTO> result = genreService.getRelationsForGenre(1);

        assertEquals("Jungle", result.getFirst().getChildGenre().getName());
    }

    @Test
    void deleteGenre_missing_throwsNotFound() {
        when(genreRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> genreService.deleteGenre(99));
    }

    @Test
    void deleteGenre_existing_removesTaxonomyRelationsFirst() {
        when(genreRepository.existsById(1)).thenReturn(true);

        genreService.deleteGenre(1);

        verify(genreRelationRepository).deleteByParentGenre_Id(1);
        verify(genreRelationRepository).deleteByChildGenre_Id(1);
        verify(genreRepository).deleteById(1);
    }

    private Genre genre(int id, String name, String slug) {
        Genre genre = new Genre(name, slug);
        ReflectionTestUtils.setField(genre, "id", id);
        return genre;
    }

    private void stubGenres(int firstId, Genre firstGenre, int secondId, Genre secondGenre) {
        when(genreRepository.findById(firstId)).thenReturn(Optional.of(firstGenre));
        when(genreRepository.findById(secondId)).thenReturn(Optional.of(secondGenre));
    }
}
