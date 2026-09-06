package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ArtistRequestDTO;
import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
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
import org.springframework.data.domain.Sort;
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
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    private Artist artist;

    @BeforeEach
    void setUp() {
        artist = new Artist();
        artist.setId(1);
        artist.setName("Four Tet");
        artist.setGenre("Electronic");
        artist.setCountry("UK");
        artist.setFestivals(new ArrayList<>());
    }

    @Test
    void getArtists_returnsPage() {
        when(artistRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(artist)));

        Page<ArtistResponseDTO> result = artistService.getArtists(0, 10, "name");

        assertEquals(1, result.getContent().size());
        assertEquals("Four Tet", result.getContent().getFirst().getName());
        verify(artistRepository).findAll(PageRequest.of(0, 10, Sort.by("name")));
    }

    @Test
    void getArtistById_found_returnsDto() {
        when(artistRepository.findById(1)).thenReturn(Optional.of(artist));

        ArtistResponseDTO result = artistService.getArtistById(1);

        assertEquals(1, result.getId());
        assertEquals("Four Tet", result.getName());
    }

    @Test
    void getArtistById_missing_throwsNotFound() {
        when(artistRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistService.getArtistById(99));
    }

    @Test
    void addArtist_savesAndReturnsDto() {
        when(artistRepository.save(any(Artist.class))).thenAnswer(invocation -> {
            Artist saved = invocation.getArgument(0);
            saved.setId(5);
            return saved;
        });

        ArtistRequestDTO request = new ArtistRequestDTO("Four Tet", "Electronic", "UK", null, null, null, null, null, null);

        ArtistResponseDTO result = artistService.addArtist(request);

        assertEquals(5, result.getId());
        assertEquals("Four Tet", result.getName());
        verify(artistRepository).save(any(Artist.class));
    }

    @Test
    void updateArtist_found_updatesFields() {
        when(artistRepository.findById(1)).thenReturn(Optional.of(artist));
        when(artistRepository.save(artist)).thenReturn(artist);

        ArtistRequestDTO request = new ArtistRequestDTO("New Name", "Jazz", "US", null, null, null, null, null, null);

        ArtistResponseDTO result = artistService.updateArtist(1, request);

        assertEquals("New Name", result.getName());
        assertEquals("Jazz", result.getGenre());
        verify(artistRepository).save(artist);
    }

    @Test
    void updateArtist_missing_throwsNotFound() {
        when(artistRepository.findById(99)).thenReturn(Optional.empty());

        ArtistRequestDTO request = new ArtistRequestDTO("New Name", "Jazz", "US", null, null, null, null, null, null);

        assertThrows(ResourceNotFoundException.class, () -> artistService.updateArtist(99, request));
    }

    @Test
    void searchArtists_returnsMatches() {
        when(artistRepository.findByNameContaining("Four")).thenReturn(List.of(artist));

        List<ArtistResponseDTO> result = artistService.searchArtists("Four");

        assertEquals(1, result.size());
        assertEquals("Four Tet", result.getFirst().getName());
    }

    @Test
    void getFestivalsOfArtist_returnsFestivalDtos() {
        Festival festival = new Festival();
        ReflectionTestUtils.setField(festival, "id", 8);
        festival.setName("Boom Festival");
        festival.setCity("Idanha");
        festival.setCountry("Portugal");
        festival.setVenue("Idanha-a-Nova");
        artist.setFestivals(List.of(festival));
        when(artistRepository.findById(1)).thenReturn(Optional.of(artist));

        List<FestivalResponseDTO> result = artistService.getFestivalsOfArtist(1);

        assertEquals(1, result.size());
        assertEquals("Boom Festival", result.getFirst().getName());
    }

    @Test
    void getFestivalsOfArtist_missingArtist_throwsNotFound() {
        when(artistRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> artistService.getFestivalsOfArtist(99));
    }

    @Test
    void deleteArtist_existing_returnsTrue() {
        when(artistRepository.existsById(1)).thenReturn(true);

        assertTrue(artistService.deleteArtist(1));
        verify(artistRepository).deleteById(1);
    }

    @Test
    void deleteArtist_missing_returnsFalse() {
        when(artistRepository.existsById(99)).thenReturn(false);

        assertFalse(artistService.deleteArtist(99));
    }
}
