package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalRequestDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FestivalServiceTest {

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private FestivalService festivalService;

    private Festival festival;
    private Artist artist;

    @BeforeEach
    void setUp() {
        festival = new Festival();
        ReflectionTestUtils.setField(festival, "id", 1);
        festival.setName("Boom Festival");
        festival.setCity("Idanha");
        festival.setCountry("Portugal");
        festival.setVenue("Idanha-a-Nova");
        festival.setStartDate(LocalDate.of(2026, 7, 18));
        festival.setEndDate(LocalDate.of(2026, 7, 25));
        festival.setTimezone("Europe/Lisbon");
        festival.setArtists(new ArrayList<>());

        artist = new Artist();
        artist.setId(20);
        artist.setName("Four Tet");
        artist.setSlug("four-tet");
        artist.setCountry("UK");
    }

    @Test
    void getFestivals_returnsPage() {
        when(festivalRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(festival)));

        Page<FestivalResponseDTO> result = festivalService.getFestivals(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("Boom Festival", result.getContent().getFirst().getName());
        verify(festivalRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void getFestivalById_found_returnsDto() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));

        FestivalResponseDTO result = festivalService.getFestivalById(1);

        assertEquals(1, result.getId());
        assertEquals("Boom Festival", result.getName());
        assertEquals("Europe/Lisbon", result.getTimezone());
    }

    @Test
    void getFestivalById_missing_throwsNotFound() {
        when(festivalRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> festivalService.getFestivalById(99));
    }

    @Test
    void addFestival_savesAndReturnsDto() {
        when(festivalRepository.existsByNameAndStartDate("Boom Festival", LocalDate.of(2026, 7, 18))).thenReturn(false);
        when(festivalRepository.save(any(Festival.class))).thenAnswer(invocation -> {
            Festival saved = invocation.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 3);
            return saved;
        });

        FestivalResponseDTO result = festivalService.addFestival(validRequest());

        assertEquals(3, result.getId());
        assertEquals("Boom Festival", result.getName());
        assertEquals("Europe/Lisbon", result.getTimezone());
    }

    @Test
    void addFestival_invalidTimezone_rejected() {
        FestivalRequestDTO request = validRequest();
        request.setTimezone("Not/AZone");

        assertThrows(InvalidRequestException.class, () -> festivalService.addFestival(request));
    }

    @Test
    void addFestival_duplicateEdition_rejected() {
        when(festivalRepository.existsByNameAndStartDate("Boom Festival", LocalDate.of(2026, 7, 18))).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> festivalService.addFestival(validRequest()));
    }

    @Test
    void addFestival_endBeforeStart_rejected() {
        FestivalRequestDTO request = validRequest();
        request.setEndDate(LocalDate.of(2026, 7, 1));

        assertThrows(InvalidRequestException.class, () -> festivalService.addFestival(request));
    }

    @Test
    void addFestival_assignsMultipleGenres() {
        Genre dnb = new Genre("Drum & Bass", "drum-and-bass");
        ReflectionTestUtils.setField(dnb, "id", 4);
        Genre jungle = new Genre("Jungle", "jungle");
        ReflectionTestUtils.setField(jungle, "id", 5);
        when(genreRepository.findById(4)).thenReturn(Optional.of(dnb));
        when(genreRepository.findById(5)).thenReturn(Optional.of(jungle));
        when(festivalRepository.existsByNameAndStartDate("Boom Festival", LocalDate.of(2026, 7, 18))).thenReturn(false);
        when(festivalRepository.save(any(Festival.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FestivalRequestDTO request = validRequest();
        request.setGenreIds(List.of(4, 5));

        FestivalResponseDTO result = festivalService.addFestival(request);

        assertEquals(2, result.getGenres().size());
    }

    @Test
    void updateFestival_found_updatesFields() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(festivalRepository.existsByNameAndStartDateAndIdNot("Updated", LocalDate.of(2026, 8, 1), 1)).thenReturn(false);
        when(festivalRepository.save(festival)).thenReturn(festival);

        FestivalRequestDTO request = new FestivalRequestDTO("Updated", "Lisbon", "Portugal", "TBA",
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2), null, null, null, "Europe/Lisbon");

        FestivalResponseDTO result = festivalService.updateFestival(1, request);

        assertEquals("Updated", result.getName());
        assertEquals("Lisbon", result.getCity());
        assertEquals("TBA", result.getVenue());
    }

    @Test
    void updateFestival_missing_throwsNotFound() {
        when(festivalRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> festivalService.updateFestival(99, validRequest()));
    }

    @Test
    void searchFestivals_returnsMatches() {
        when(festivalRepository.findByNameContaining("Boom")).thenReturn(List.of(festival));

        List<FestivalResponseDTO> result = festivalService.searchFestivals("Boom");

        assertEquals(1, result.size());
        assertEquals("Boom Festival", result.getFirst().getName());
    }

    @Test
    void getArtistsOfFestival_returnsArtistDtos() {
        festival.getArtists().add(artist);
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));

        List<ArtistResponseDTO> result = festivalService.getArtistsOfFestival(1);

        assertEquals(1, result.size());
        assertEquals("Four Tet", result.getFirst().getName());
    }

    @Test
    void getArtistsOfFestival_missingFestival_throwsNotFound() {
        when(festivalRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> festivalService.getArtistsOfFestival(99));
    }

    @Test
    void addFestival_sameDayEdition_isAllowed() {
        when(festivalRepository.existsByNameAndStartDate("Boom Festival", LocalDate.of(2026, 7, 18))).thenReturn(false);
        when(festivalRepository.save(any(Festival.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FestivalRequestDTO request = validRequest();
        request.setEndDate(LocalDate.of(2026, 7, 18));

        FestivalResponseDTO result = festivalService.addFestival(request);

        assertEquals(LocalDate.of(2026, 7, 18), result.getEndDate());
    }

    @Test
    void addArtistToFestival_doesNotRequireAPerformance() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));
        when(festivalRepository.save(festival)).thenReturn(festival);

        festivalService.addArtistToFestival(1, 20);

        assertEquals(1, festival.getArtists().size());
        assertEquals(20, festival.getArtists().getFirst().getId());
    }

    @Test
    void addArtistToFestival_savesRelation() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));
        when(festivalRepository.save(festival)).thenReturn(festival);

        FestivalResponseDTO result = festivalService.addArtistToFestival(1, 20);

        assertEquals(1, festival.getArtists().size());
        assertEquals("Boom Festival", result.getName());
    }

    @Test
    void addArtistToFestival_duplicate_rejected() {
        festival.getArtists().add(artist);
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));

        assertThrows(DuplicateResourceException.class, () -> festivalService.addArtistToFestival(1, 20));
    }

    @Test
    void addArtistToFestival_missingFestival_throwsNotFound() {
        when(festivalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> festivalService.addArtistToFestival(1, 20));
    }

    @Test
    void addArtistToFestival_missingArtist_throwsNotFound() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(artistRepository.findById(20)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> festivalService.addArtistToFestival(1, 20));
    }

    @Test
    void deleteArtistFestival_existing_returnsTrue() {
        festival.getArtists().add(artist);
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));

        assertTrue(festivalService.deleteArtistFestival(1, 20));
        verify(festivalRepository).save(festival);
    }

    @Test
    void deleteArtistFestival_missingFestivalOrArtist_returnsFalse() {
        when(festivalRepository.findById(1)).thenReturn(Optional.empty());
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));

        assertFalse(festivalService.deleteArtistFestival(1, 20));
    }

    @Test
    void deleteFestival_existing_returnsTrue() {
        when(festivalRepository.existsById(1)).thenReturn(true);

        assertTrue(festivalService.deleteFestival(1));
        verify(festivalRepository).deleteById(1);
    }

    @Test
    void deleteFestival_missing_returnsFalse() {
        when(festivalRepository.existsById(99)).thenReturn(false);

        assertFalse(festivalService.deleteFestival(99));
    }

    private FestivalRequestDTO validRequest() {
        return new FestivalRequestDTO("Boom Festival", "Idanha", "Portugal", "Idanha-a-Nova",
                LocalDate.of(2026, 7, 18), LocalDate.of(2026, 7, 25), null, null, null, "Europe/Lisbon");
    }
}
