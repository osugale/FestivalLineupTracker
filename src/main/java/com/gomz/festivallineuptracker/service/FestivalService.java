package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalRequestDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.dto.ResponseMapper;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.model.GenreRelation;
import com.gomz.festivallineuptracker.model.UserArtistFavorite;
import com.gomz.festivallineuptracker.model.UserGenrePreference;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.GenreRelationRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import com.gomz.festivallineuptracker.repository.UserArtistFavoriteRepository;
import com.gomz.festivallineuptracker.repository.UserGenrePreferenceRepository;
import com.gomz.festivallineuptracker.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final GenreRelationRepository genreRelationRepository;
    private final UserGenrePreferenceRepository userGenrePreferenceRepository;
    private final UserArtistFavoriteRepository userArtistFavoriteRepository;

    public FestivalService(FestivalRepository festivalRepository, ArtistRepository artistRepository,
                           GenreRepository genreRepository, GenreRelationRepository genreRelationRepository,
                           UserGenrePreferenceRepository userGenrePreferenceRepository,
                           UserArtistFavoriteRepository userArtistFavoriteRepository) {
        this.festivalRepository = festivalRepository;
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
        this.genreRelationRepository = genreRelationRepository;
        this.userGenrePreferenceRepository = userGenrePreferenceRepository;
        this.userArtistFavoriteRepository = userArtistFavoriteRepository;
    }

    public Page<FestivalResponseDTO> getFestivals(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PersonalizationContext context = personalizationContext();
        if (context == null) {
            return festivalRepository.findAll(pageable).map(ResponseMapper::toFestivalResponse);
        }

        List<Festival> allFestivals = new ArrayList<>(festivalRepository.findAll());
        Map<Integer, Set<Integer>> festivalGenreIds = festivalGenreIds();
        allFestivals.sort(personalizedOrder(context, festivalGenreIds));

        int from = Math.min(page * size, allFestivals.size());
        int to = Math.min(from + size, allFestivals.size());
        List<Festival> pageFestivals = allFestivals.subList(from, to);
        Map<Integer, Festival> withGenres = pageFestivals.isEmpty()
                ? Map.of()
                : festivalRepository.findWithGenresByIdIn(pageFestivals.stream().map(Festival::getId).toList()).stream()
                .collect(Collectors.toMap(Festival::getId, Function.identity()));

        List<FestivalResponseDTO> content = new ArrayList<>();
        for (Festival festival : pageFestivals) {
            Festival hydrated = withGenres.getOrDefault(festival.getId(), festival);
            content.add(toPersonalizedResponse(hydrated, context, festivalGenreIds));
        }
        return new PageImpl<>(content, pageable, allFestivals.size());
    }

    public FestivalResponseDTO getFestivalById(int id) {
        Festival festival = findFestival(id);
        PersonalizationContext context = personalizationContext();
        if (context == null) {
            return ResponseMapper.toFestivalResponse(festival);
        }
        return toPersonalizedResponse(festival, context, festivalGenreIds());
    }

    public List<FestivalResponseDTO> searchFestivals(String name) {
        return festivalRepository.findByNameContaining(name).stream().map(ResponseMapper::toFestivalResponse).toList();
    }

    public FestivalResponseDTO addFestival(FestivalRequestDTO dto) {
        validateDateRange(dto);
        String timezone = requireValidTimezone(dto.getTimezone());
        assertEditionAvailable(dto.getName(), dto.getStartDate(), null);

        Festival festival = new Festival();
        applyFestivalFields(festival, dto, timezone);
        festival.setGenres(resolveGenres(dto.getGenreIds()));

        return ResponseMapper.toFestivalResponse(festivalRepository.save(festival));
    }

    public FestivalResponseDTO addArtistToFestival(int festivalId, int artistId) {
        Festival festival = findFestival(festivalId);
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist with id " + artistId + " not found"));

        boolean alreadyAdded = festival.getArtists().stream().anyMatch(existing -> existing.getId() == artist.getId());
        if (alreadyAdded) {
            throw new DuplicateResourceException("Artist is already on this festival lineup");
        }

        festival.getArtists().add(artist);
        return ResponseMapper.toFestivalResponse(festivalRepository.save(festival));
    }

    public FestivalResponseDTO updateFestival(int id, FestivalRequestDTO dto) {
        Festival festival = findFestival(id);
        validateDateRange(dto);
        String timezone = requireValidTimezone(dto.getTimezone());
        assertEditionAvailable(dto.getName(), dto.getStartDate(), id);

        applyFestivalFields(festival, dto, timezone);
        festival.setGenres(resolveGenres(dto.getGenreIds()));

        return ResponseMapper.toFestivalResponse(festivalRepository.save(festival));
    }

    public Boolean deleteArtistFestival(int festivalId, int artistId) {
        Festival festival = festivalRepository.findById(festivalId).orElse(null);
        Artist artist = artistRepository.findById(artistId).orElse(null);

        if (festival == null || artist == null) {
            return false;
        }

        festival.getArtists().removeIf(existing -> existing.getId() == artist.getId());
        festivalRepository.save(festival);
        return true;
    }

    public List<ArtistResponseDTO> getArtistsOfFestival(int festivalId) {
        Festival festival = findFestival(festivalId);
        return festival.getArtists().stream().map(ResponseMapper::toArtistResponse).toList();
    }

    public boolean deleteFestival(int id) {
        if (!festivalRepository.existsById(id)) {
            return false;
        }
        festivalRepository.deleteById(id);
        return true;
    }













    private Festival findFestival(int id) {
        return festivalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Festival with id " + id + " not found"));
    }

    private void applyFestivalFields(Festival festival, FestivalRequestDTO dto, String timezone) {
        festival.setName(dto.getName().trim());
        festival.setCity(dto.getCity().trim());
        festival.setCountry(dto.getCountry().trim());
        festival.setVenue(dto.getVenue().trim());
        festival.setStartDate(dto.getStartDate());
        festival.setEndDate(dto.getEndDate());
        festival.setTimezone(timezone);
        festival.setDescription(dto.getDescription());
        festival.setImageUrl(dto.getImageUrl());
        festival.setOfficialWebsite(dto.getOfficialWebsite());
    }

    private void validateDateRange(FestivalRequestDTO dto) {
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new InvalidRequestException("Festival start date and end date are required");
        }
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new InvalidRequestException("endDate must be on or after startDate");
        }
    }

    private String requireValidTimezone(String timezone) {
        if (timezone == null || timezone.isBlank()) {
            throw new InvalidRequestException("Festival timezone is required");
        }
        String trimmed = timezone.trim();
        try {
            ZoneId.of(trimmed);
        } catch (DateTimeException ex) {
            throw new InvalidRequestException("Invalid IANA timezone: " + trimmed);
        }
        return trimmed;
    }

    private void assertEditionAvailable(String name, java.time.LocalDate startDate, Integer excludeId) {
        boolean taken = excludeId == null
                ? festivalRepository.existsByNameAndStartDate(name.trim(), startDate)
                : festivalRepository.existsByNameAndStartDateAndIdNot(name.trim(), startDate, excludeId);
        if (taken) {
            throw new DuplicateResourceException("Festival edition '" + name.trim() + "' on " + startDate + " already exists");
        }
    }

    private Set<Genre> resolveGenres(List<Integer> genreIds) {
        Set<Integer> uniqueIds = new LinkedHashSet<>();
        if (genreIds != null) {
            for (Integer genreId : genreIds) {
                if (genreId != null) {
                    uniqueIds.add(genreId);
                }
            }
        }
        Set<Genre> genres = new LinkedHashSet<>();
        for (Integer genreId : uniqueIds) {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new ResourceNotFoundException("Genre with id " + genreId + " not found"));
            genres.add(genre);
        }
        return genres;
    }

    private PersonalizationContext personalizationContext() {
        Integer userId = CurrentUser.idOrNull();
        if (userId == null) {
            return null;
        }
        Set<Integer> parentIds = userGenrePreferenceRepository.findByUserId(userId).stream()
                .map(UserGenrePreference::getGenreId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (parentIds.isEmpty()) {
            return null;
        }
        List<Integer> favoriteArtistIds = userArtistFavoriteRepository.findByUserId(userId).stream()
                .map(UserArtistFavorite::getArtistId)
                .toList();
        Set<Integer> festivalsWithFavorites = new HashSet<>();
        if (!favoriteArtistIds.isEmpty()) {
            festivalsWithFavorites.addAll(festivalRepository.findFestivalIdsByArtistIds(favoriteArtistIds));
        }
        return new PersonalizationContext(parentIds, childToParent(), festivalsWithFavorites);
    }

    private Map<Integer, Integer> childToParent() {
        Map<Integer, Integer> childToParent = new HashMap<>();
        for (GenreRelation relation : genreRelationRepository.findAll()) {
            childToParent.put(relation.getChildGenre().getId(), relation.getParentGenre().getId());
        }
        return childToParent;
    }

    private Map<Integer, Set<Integer>> festivalGenreIds() {
        Map<Integer, Set<Integer>> festivalGenreIds = new HashMap<>();
        for (Object[] row : festivalRepository.findFestivalGenreIds()) {
            int festivalId = ((Number) row[0]).intValue();
            int genreId = ((Number) row[1]).intValue();
            festivalGenreIds.computeIfAbsent(festivalId, ignored -> new HashSet<>()).add(genreId);
        }
        return festivalGenreIds;
    }

    private Comparator<Festival> personalizedOrder(PersonalizationContext context, Map<Integer, Set<Integer>> festivalGenreIds) {
        return Comparator
                .comparingInt((Festival festival) -> -fit(festival.getId(), context, festivalGenreIds))
                .thenComparing((Festival festival) -> !context.festivalsWithFavorites.contains(festival.getId()))
                .thenComparing(Festival::getName, String.CASE_INSENSITIVE_ORDER);
    }

    private FestivalResponseDTO toPersonalizedResponse(Festival festival, PersonalizationContext context,
                                                       Map<Integer, Set<Integer>> festivalGenreIds) {
        return ResponseMapper.toFestivalResponse(
                festival,
                fit(festival.getId(), context, festivalGenreIds),
                context.festivalsWithFavorites.contains(festival.getId())
        );
    }

    private int fit(int festivalId, PersonalizationContext context, Map<Integer, Set<Integer>> festivalGenreIds) {
        Set<Integer> festivalRoots = FestivalFitCalculator.collapseToRoots(
                festivalGenreIds.getOrDefault(festivalId, Set.of()),
                context.childToParent
        );
        return FestivalFitCalculator.fitPercent(context.parentIds, festivalRoots);
    }

    private record PersonalizationContext(Set<Integer> parentIds, Map<Integer, Integer> childToParent,
                                          Set<Integer> festivalsWithFavorites) {
    }
}
