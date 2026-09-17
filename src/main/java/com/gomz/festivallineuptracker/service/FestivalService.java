package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.ResponseMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;

    public FestivalService(FestivalRepository festivalRepository, ArtistRepository artistRepository,
                           GenreRepository genreRepository) {
        this.festivalRepository = festivalRepository;
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
    }









    public Page<FestivalResponseDTO> getFestivals(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return festivalRepository.findAll(pageable).map(ResponseMapper::toFestivalResponse);
    }





    public FestivalResponseDTO getFestivalById(int id) {
        return ResponseMapper.toFestivalResponse(findFestival(id));
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

        festival.getArtists().remove(artist);
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
        festival.setTimetableUrl(dto.getTimetableUrl());
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
}
