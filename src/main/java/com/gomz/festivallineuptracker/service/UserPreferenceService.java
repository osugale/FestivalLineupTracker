package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FavoriteArtistsRequestDTO;
import com.gomz.festivallineuptracker.dto.GenrePreferenceRequestDTO;
import com.gomz.festivallineuptracker.dto.GenreResponseDTO;
import com.gomz.festivallineuptracker.dto.ResponseMapper;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.model.GenreRelation;
import com.gomz.festivallineuptracker.model.UserArtistFavorite;
import com.gomz.festivallineuptracker.model.UserGenrePreference;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.GenreRelationRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import com.gomz.festivallineuptracker.repository.UserArtistFavoriteRepository;
import com.gomz.festivallineuptracker.repository.UserGenrePreferenceRepository;
import com.gomz.festivallineuptracker.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserPreferenceService {

    private final UserGenrePreferenceRepository userGenrePreferenceRepository;
    private final UserArtistFavoriteRepository userArtistFavoriteRepository;
    private final GenreRepository genreRepository;
    private final GenreRelationRepository genreRelationRepository;
    private final ArtistRepository artistRepository;



    public UserPreferenceService(UserGenrePreferenceRepository userGenrePreferenceRepository, UserArtistFavoriteRepository userArtistFavoriteRepository, GenreRepository genreRepository, GenreRelationRepository genreRelationRepository, ArtistRepository artistRepository) {
        this.userGenrePreferenceRepository = userGenrePreferenceRepository;this.userArtistFavoriteRepository = userArtistFavoriteRepository;this.genreRepository = genreRepository;this.genreRelationRepository = genreRelationRepository;this.artistRepository = artistRepository;
    }














    public List<GenreResponseDTO> getGenrePreferences() {
        int userId = CurrentUser.requireId();
        Set<Integer> genreIds = preferredGenreIds(userId);
        if (genreIds.isEmpty()) {
            return List.of();
        }
        return genreRepository.findAllById(genreIds).stream().map(ResponseMapper::toGenreResponse).sorted(Comparator.comparing(GenreResponseDTO::getName,
                String.CASE_INSENSITIVE_ORDER)).toList();
    }












    public List<GenreResponseDTO> replaceGenrePreferences(GenrePreferenceRequestDTO request) {
        int userId = CurrentUser.requireId();
        List<Integer> ids = request.getGenreIds();
        assertNoDuplicates(ids, "genreIds");
        if (ids.size() < 3 || ids.size() > 5) {
            throw new InvalidRequestException("Select between 3 and 5 genres");
        }

        Set<Integer> rootIds = rootGenreIds();
        List<Genre> genres = new ArrayList<>();
        for (Integer genreId : ids) {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new ResourceNotFoundException("Genre with id " + genreId + " not found"));
            if (!rootIds.contains(genre.getId())) {
                throw new InvalidRequestException("genreIds must be parent genres");
            }
            genres.add(genre);
        }

        userGenrePreferenceRepository.deleteByUserId(userId);
        userGenrePreferenceRepository.flush();
        for (Genre genre : genres) {
            userGenrePreferenceRepository.save(new UserGenrePreference(userId, genre.getId()));
        }
        genres.sort(Comparator.comparing(Genre::getName, String.CASE_INSENSITIVE_ORDER));
        return genres.stream().map(ResponseMapper::toGenreResponse).toList();
    }









    public List<ArtistResponseDTO> getOnboardingArtists() {
        int userId = CurrentUser.requireId();
        Set<Integer> parentIds = preferredGenreIds(userId);
        if (parentIds.isEmpty()) {
            throw new InvalidRequestException("Select genres first");
        }
        return eligibleArtists(parentIds).stream().map(ResponseMapper::toArtistResponse).toList();
    }











    public List<ArtistResponseDTO> replaceOnboardingFavorites(FavoriteArtistsRequestDTO request) {
        int userId = CurrentUser.requireId();
        Set<Integer> parentIds = preferredGenreIds(userId);
        if (parentIds.isEmpty()) {
            throw new InvalidRequestException("Select genres first");
        }
        List<Integer> artistIds = request.getArtistIds();
        assertNoDuplicates(artistIds, "artistIds");
        if (artistIds.size() != 5) {
            throw new InvalidRequestException("Onboarding favorites must contain exactly 5 unique eligible artists");
        }
        Set<Integer> eligibleIds = eligibleArtists(parentIds).stream().map(Artist::getId).collect(Collectors.toSet());
        List<Artist> artists = resolveFavoriteArtists(artistIds, eligibleIds, true);
        replaceFavorites(userId, artists);
        return artists.stream().map(ResponseMapper::toArtistResponse).toList();
    }











    public List<ArtistResponseDTO> getFavoriteArtists() {
        int userId = CurrentUser.requireId();
        List<Integer> artistIds = userArtistFavoriteRepository.findByUserId(userId).stream()
                .map(UserArtistFavorite::getArtistId)
                .toList();
        if (artistIds.isEmpty()) {
            return List.of();
        }
        return artistRepository.findWithGenresByIdIn(artistIds).stream()
                .map(ResponseMapper::toArtistResponse)
                .sorted(Comparator.comparing(ArtistResponseDTO::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }













    public List<ArtistResponseDTO> replaceFavoriteArtists(FavoriteArtistsRequestDTO request) {
        int userId = CurrentUser.requireId();
        List<Integer> artistIds = request.getArtistIds();
        assertNoDuplicates(artistIds, "artistIds");
        List<Artist> artists = resolveFavoriteArtists(artistIds, null, false);
        replaceFavorites(userId, artists);
        return artists.stream().map(ResponseMapper::toArtistResponse).toList();
    }























    private void replaceFavorites(int userId, List<Artist> artists) {
        userArtistFavoriteRepository.deleteByUserId(userId);
        userArtistFavoriteRepository.flush();
        for (Artist artist : artists) {
            userArtistFavoriteRepository.save(new UserArtistFavorite(userId, artist.getId()));
        }
    }

    private List<Artist> resolveFavoriteArtists(List<Integer> artistIds, Set<Integer> eligibleIds, boolean requireEligible) {
        List<Artist> artists = new ArrayList<>();
        for (Integer artistId : artistIds) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new ResourceNotFoundException("Artist with id " + artistId + " not found"));
            if (requireEligible && !eligibleIds.contains(artist.getId())) {
                throw new InvalidRequestException("Artist with id " + artistId + " is not eligible for the selected genres");
            }
            artists.add(artist);
        }
        return artists;
    }

    private List<Artist> eligibleArtists(Set<Integer> parentIds) {
        Map<Integer, Integer> childToParent = childToParent();
        Set<Integer> matchingGenreIds = new HashSet<>();
        for (Genre genre : genreRepository.findAll()) {
            if (parentIds.contains(FestivalFitCalculator.resolveRoot(genre.getId(), childToParent))) {
                matchingGenreIds.add(genre.getId());
            }
        }
        if (matchingGenreIds.isEmpty()) {
            return List.of();
        }
        List<Artist> artists = new ArrayList<>(artistRepository.findDistinctByGenreIds(matchingGenreIds));
        artists.sort(Comparator.comparing(Artist::getName, String.CASE_INSENSITIVE_ORDER));
        return artists;
    }

    Set<Integer> preferredGenreIds(int userId) {
        return userGenrePreferenceRepository.findByUserId(userId).stream()
                .map(UserGenrePreference::getGenreId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    Map<Integer, Integer> childToParent() {
        Map<Integer, Integer> childToParent = new HashMap<>();
        for (GenreRelation relation : genreRelationRepository.findAll()) {
            childToParent.put(relation.getChildGenre().getId(), relation.getParentGenre().getId());
        }
        return childToParent;
    }

    private Set<Integer> rootGenreIds() {
        return genreRepository.findRootGenres().stream().map(Genre::getId).collect(Collectors.toSet());
    }

    private void assertNoDuplicates(List<Integer> ids, String fieldName) {
        if (ids == null) {
            throw new InvalidRequestException(fieldName + " is required");
        }
        Set<Integer> unique = new HashSet<>();
        for (Integer id : ids) {
            if (id == null) {
                throw new InvalidRequestException(fieldName + " must not contain null");
            }
            if (!unique.add(id)) {
                throw new InvalidRequestException("Duplicate " + fieldName + " are not allowed");
            }
        }
    }
}
