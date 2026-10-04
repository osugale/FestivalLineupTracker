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
import com.gomz.festivallineuptracker.model.Role;
import com.gomz.festivallineuptracker.model.User;
import com.gomz.festivallineuptracker.model.UserArtistFavorite;
import com.gomz.festivallineuptracker.model.UserGenrePreference;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.GenreRelationRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import com.gomz.festivallineuptracker.repository.UserArtistFavoriteRepository;
import com.gomz.festivallineuptracker.repository.UserGenrePreferenceRepository;
import com.gomz.festivallineuptracker.security.AppUserDetails;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Mock
    private GenreRelationRepository genreRelationRepository;

    @Mock
    private UserGenrePreferenceRepository userGenrePreferenceRepository;

    @Mock
    private UserArtistFavoriteRepository userArtistFavoriteRepository;

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
        festival.setArtists(new HashSet<>());

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
        assertEquals(20, festival.getArtists().iterator().next().getId());
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

    @Test
    void getFestivals_authenticatedWithoutPreferences_usesUnpersonalizedPage() {
        authenticate(7);
        when(userGenrePreferenceRepository.findByUserId(7)).thenReturn(List.of());
        when(festivalRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(festival)));

        Page<FestivalResponseDTO> result = festivalService.getFestivals(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(null, result.getContent().getFirst().getFestivalFit());
        assertEquals(null, result.getContent().getFirst().getHasFavoriteArtist());
        verify(festivalRepository).findAll(PageRequest.of(0, 10));
        SecurityContextHolder.clearContext();
    }

    @Test
    void getFestivals_withPreferences_sortsByFitThenFavoriteThenName() {
        authenticate(7);
        when(userGenrePreferenceRepository.findByUserId(7)).thenReturn(List.of(
                new UserGenrePreference(7, 1),
                new UserGenrePreference(7, 2),
                new UserGenrePreference(7, 3)
        ));
        when(userArtistFavoriteRepository.findByUserId(7)).thenReturn(List.of(new UserArtistFavorite(7, 20)));
        when(genreRelationRepository.findAll()).thenReturn(List.of());

        Festival lowFitFavorite = festivalNamed(1, "Alpha");
        Festival highFit = festivalNamed(2, "Zulu");
        Festival midFit = festivalNamed(3, "Mike");
        when(festivalRepository.findAll()).thenReturn(List.of(lowFitFavorite, highFit, midFit));
        when(festivalRepository.findFestivalGenreIds()).thenReturn(List.<Object[]>of(
                new Object[]{1, 1},
                new Object[]{2, 1},
                new Object[]{2, 2},
                new Object[]{2, 3},
                new Object[]{3, 1},
                new Object[]{3, 2}
        ));
        when(festivalRepository.findFestivalIdsByArtistIds(any())).thenReturn(List.of(1));
        when(festivalRepository.findWithGenresByIdIn(any())).thenReturn(List.of(highFit, midFit, lowFitFavorite));

        Page<FestivalResponseDTO> result = festivalService.getFestivals(0, 10);

        assertEquals(List.of("Zulu", "Mike", "Alpha"), result.getContent().stream().map(FestivalResponseDTO::getName).toList());
        assertEquals(100, result.getContent().get(0).getFestivalFit());
        assertEquals(67, result.getContent().get(1).getFestivalFit());
        assertEquals(33, result.getContent().get(2).getFestivalFit());
        assertEquals(false, result.getContent().get(0).getHasFavoriteArtist());
        assertEquals(true, result.getContent().get(2).getHasFavoriteArtist());
        SecurityContextHolder.clearContext();
    }

    @Test
    void getFestivalById_withPreferences_includesOverlay() {
        authenticate(7);
        festival.setGenres(Set.of());
        when(festivalRepository.findById(1)).thenReturn(Optional.of(festival));
        when(userGenrePreferenceRepository.findByUserId(7)).thenReturn(List.of(
                new UserGenrePreference(7, 1),
                new UserGenrePreference(7, 2),
                new UserGenrePreference(7, 3)
        ));
        when(userArtistFavoriteRepository.findByUserId(7)).thenReturn(List.of());
        when(genreRelationRepository.findAll()).thenReturn(List.of());
        when(festivalRepository.findFestivalGenreIds()).thenReturn(List.<Object[]>of(new Object[]{1, 1}));

        FestivalResponseDTO result = festivalService.getFestivalById(1);

        assertEquals(33, result.getFestivalFit());
        assertEquals(false, result.getHasFavoriteArtist());
        SecurityContextHolder.clearContext();
    }

    private Festival festivalNamed(int id, String name) {
        Festival named = new Festival();
        ReflectionTestUtils.setField(named, "id", id);
        named.setName(name);
        named.setCity("Idanha");
        named.setCountry("Portugal");
        named.setVenue("Field");
        named.setStartDate(LocalDate.of(2026, 7, 18));
        named.setEndDate(LocalDate.of(2026, 7, 25));
        named.setTimezone("Europe/Lisbon");
        return named;
    }

    private void authenticate(int userId) {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        user.setUsername("om");
        user.setEmail("om@example.com");
        user.setPassword("secret");
        user.setRole(Role.USER);
        AppUserDetails details = new AppUserDetails(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities()));
    }

    private FestivalRequestDTO validRequest() {
        return new FestivalRequestDTO("Boom Festival", "Idanha", "Portugal", "Idanha-a-Nova",
                LocalDate.of(2026, 7, 18), LocalDate.of(2026, 7, 25), null, null, null, "Europe/Lisbon");
    }
}
