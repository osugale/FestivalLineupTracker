package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalRequestDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
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
        festival.setArtists(new ArrayList<>());

        artist = new Artist();
        artist.setId(20);
        artist.setName("Four Tet");
        artist.setGenre("Electronic");
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
    }

    @Test
    void getFestivalById_missing_throwsNotFound() {
        when(festivalRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> festivalService.getFestivalById(99));
    }

    @Test
    void addFestival_savesAndReturnsDto() {
        when(festivalRepository.save(any(Festival.class))).thenAnswer(invocation -> {
            Festival saved = invocation.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 3);
            return saved;
        });

        FestivalRequestDTO request = new FestivalRequestDTO("Boom Festival", "Idanha", "Portugal", "Idanha-a-Nova",
                null, null, null, null, null, "Electronic");

        FestivalResponseDTO result = festivalService.addFestival(request);

        assertEquals(3, result.getId());
        assertEquals("Boom Festival", result.getName());
    }

    @Test
    void updateFestival_found_updatesFields() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(festivalRepository.save(festival)).thenReturn(festival);

        FestivalRequestDTO request = new FestivalRequestDTO("Updated", "Lisbon", "Portugal", "Venue",
                null, null, null, null, null, "Rock");

        FestivalResponseDTO result = festivalService.updateFestival(1, request);

        assertEquals("Updated", result.getName());
        assertEquals("Lisbon", result.getCity());
    }

    @Test
    void updateFestival_missing_throwsNotFound() {
        when(festivalRepository.findById(99)).thenReturn(Optional.empty());

        FestivalRequestDTO request = new FestivalRequestDTO("Updated", "Lisbon", "Portugal", "Venue",
                null, null, null, null, null, "Rock");

        assertThrows(ResourceNotFoundException.class, () -> festivalService.updateFestival(99, request));
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
    void addArtistToFestival_savesRelation() {
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));
        when(festivalRepository.save(festival)).thenReturn(festival);

        FestivalResponseDTO result = festivalService.addArtistToFestival(1, 20);

        assertEquals(1, festival.getArtists().size());
        assertEquals("Boom Festival", result.getName());
    }

    @Test
    void addArtistToFestival_missingFestival_throwsNotFound() {
        when(festivalRepository.findById(1)).thenReturn(Optional.empty());
        when(artistRepository.findById(20)).thenReturn(Optional.of(artist));

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
}
