package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.FavoriteArtistsRequestDTO;
import com.gomz.festivallineuptracker.dto.GenrePreferenceRequestDTO;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.model.Role;
import com.gomz.festivallineuptracker.model.User;
import com.gomz.festivallineuptracker.model.UserGenrePreference;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.GenreRelationRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import com.gomz.festivallineuptracker.repository.UserArtistFavoriteRepository;
import com.gomz.festivallineuptracker.repository.UserGenrePreferenceRepository;
import com.gomz.festivallineuptracker.security.AppUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPreferenceServiceTest {

    @Mock
    private UserGenrePreferenceRepository userGenrePreferenceRepository;

    @Mock
    private UserArtistFavoriteRepository userArtistFavoriteRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreRelationRepository genreRelationRepository;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private UserPreferenceService userPreferenceService;

    @BeforeEach
    void authenticate() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 7);
        user.setUsername("om");
        user.setEmail("om@example.com");
        user.setPassword("secret");
        user.setRole(Role.USER);
        AppUserDetails details = new AppUserDetails(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities()));
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void replaceGenrePreferences_rejectsDuplicates() {
        GenrePreferenceRequestDTO request = new GenrePreferenceRequestDTO();
        request.setGenreIds(List.of(1, 2, 1));

        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> userPreferenceService.replaceGenrePreferences(request));
        assertTrue(ex.getMessage().toLowerCase().contains("duplicate"));
        verify(userGenrePreferenceRepository, never()).deleteByUserId(7);
    }

    @Test
    void replaceGenrePreferences_rejectsCountOutside3To5() {
        GenrePreferenceRequestDTO request = new GenrePreferenceRequestDTO();
        request.setGenreIds(List.of(1, 2));

        assertThrows(InvalidRequestException.class, () -> userPreferenceService.replaceGenrePreferences(request));
    }

    @Test
    void replaceGenrePreferences_rejectsChildGenres() {
        Genre parent = genre(1, "Electronic");
        Genre child = genre(9, "Techno");
        when(genreRepository.findRootGenres()).thenReturn(List.of(parent));
        when(genreRepository.findById(9)).thenReturn(Optional.of(child));
        GenrePreferenceRequestDTO request = new GenrePreferenceRequestDTO();
        request.setGenreIds(List.of(9, 1, 2));

        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> userPreferenceService.replaceGenrePreferences(request));
        assertTrue(ex.getMessage().toLowerCase().contains("parent"));
    }

    @Test
    void replaceGenrePreferences_savesThreeParents() {
        Genre a = genre(1, "Electronic");
        Genre b = genre(2, "Hip Hop");
        Genre c = genre(3, "Rock");
        when(genreRepository.findRootGenres()).thenReturn(List.of(a, b, c));
        when(genreRepository.findById(1)).thenReturn(Optional.of(a));
        when(genreRepository.findById(2)).thenReturn(Optional.of(b));
        when(genreRepository.findById(3)).thenReturn(Optional.of(c));
        when(userGenrePreferenceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GenrePreferenceRequestDTO request = new GenrePreferenceRequestDTO();
        request.setGenreIds(List.of(1, 2, 3));

        assertEquals(3, userPreferenceService.replaceGenrePreferences(request).size());
        verify(userGenrePreferenceRepository).deleteByUserId(7);
        verify(userGenrePreferenceRepository).flush();
    }

    @Test
    void getOnboardingArtists_requiresSavedGenres() {
        when(userGenrePreferenceRepository.findByUserId(7)).thenReturn(List.of());

        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> userPreferenceService.getOnboardingArtists());
        assertEquals("Select genres first", ex.getMessage());
    }

    @Test
    void replaceOnboardingFavorites_replacesExactFiveEligibleArtists() {
        when(userGenrePreferenceRepository.findByUserId(7)).thenReturn(List.of(new UserGenrePreference(7, 1)));
        Genre electronic = genre(1, "Electronic");
        when(genreRepository.findAll()).thenReturn(List.of(electronic));
        when(genreRelationRepository.findAll()).thenReturn(List.of());
        List<Artist> eligible = List.of(artist(11, "A"), artist(12, "B"), artist(13, "C"), artist(14, "D"), artist(15, "E"));
        when(artistRepository.findDistinctByGenreIds(any())).thenReturn(eligible);
        for (Artist artist : eligible) {
            when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        }
        when(userArtistFavoriteRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        FavoriteArtistsRequestDTO request = new FavoriteArtistsRequestDTO();
        request.setArtistIds(List.of(11, 12, 13, 14, 15));

        assertEquals(5, userPreferenceService.replaceOnboardingFavorites(request).size());
        assertEquals(5, userPreferenceService.replaceOnboardingFavorites(request).size());
        verify(userArtistFavoriteRepository, org.mockito.Mockito.times(2)).deleteByUserId(7);
    }

    @Test
    void replaceFavoriteArtists_allowsEmptyList() {
        FavoriteArtistsRequestDTO request = new FavoriteArtistsRequestDTO();
        request.setArtistIds(List.of());

        assertTrue(userPreferenceService.replaceFavoriteArtists(request).isEmpty());
        verify(userArtistFavoriteRepository).deleteByUserId(7);
        verify(artistRepository, never()).findById(any());
    }

    @Test
    void replaceOnboardingFavorites_rejectsUnknownArtist() {
        when(userGenrePreferenceRepository.findByUserId(7)).thenReturn(List.of(new UserGenrePreference(7, 1)));
        when(genreRepository.findAll()).thenReturn(List.of(genre(1, "Electronic")));
        when(genreRelationRepository.findAll()).thenReturn(List.of());
        when(artistRepository.findDistinctByGenreIds(any())).thenReturn(List.of());
        when(artistRepository.findById(99)).thenReturn(Optional.empty());

        FavoriteArtistsRequestDTO request = new FavoriteArtistsRequestDTO();
        request.setArtistIds(List.of(99, 1, 2, 3, 4));

        assertThrows(ResourceNotFoundException.class, () -> userPreferenceService.replaceOnboardingFavorites(request));
    }

    private static Genre genre(int id, String name) {
        Genre genre = new Genre(name, name.toLowerCase().replace(' ', '-'));
        ReflectionTestUtils.setField(genre, "id", id);
        return genre;
    }

    private static Artist artist(int id, String name) {
        Artist artist = new Artist();
        artist.setId(id);
        artist.setName(name);
        artist.setSlug(name.toLowerCase());
        artist.setCountry("UK");
        return artist;
    }
}
