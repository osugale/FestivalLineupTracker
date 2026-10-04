package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ArtistRequestDTO;
import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.ResponseMapper;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceInUseException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import com.gomz.festivallineuptracker.repository.PerformanceRepository;
import com.gomz.festivallineuptracker.repository.UserArtistFavoriteRepository;
import com.gomz.festivallineuptracker.util.SlugNormalizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final PerformanceRepository performanceRepository;
    private final FestivalRepository festivalRepository;
    private final UserArtistFavoriteRepository userArtistFavoriteRepository;

    public ArtistService(ArtistRepository artistRepository, GenreRepository genreRepository,
                         PerformanceRepository performanceRepository, FestivalRepository festivalRepository,
                         UserArtistFavoriteRepository userArtistFavoriteRepository) {
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
        this.performanceRepository = performanceRepository;
        this.festivalRepository = festivalRepository;
        this.userArtistFavoriteRepository = userArtistFavoriteRepository;
    }




    public Page<ArtistResponseDTO> getArtists(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return mapArtistsWithGenres(artistRepository.findAll(pageable));
    }




    public ArtistResponseDTO getArtistById(int id) {
        return ResponseMapper.toArtistResponse(findArtist(id));
    }





    public ArtistResponseDTO addArtist(ArtistRequestDTO artistDTO) {
        String slug = requireSlug(artistDTO.getName());
        assertSlugAvailable(slug, null);

        Artist artist = new Artist();
        applyArtistFields(artist, artistDTO, slug);
        artist.setGenres(resolveGenres(artistDTO.getGenreIds()));

        return ResponseMapper.toArtistResponse(artistRepository.save(artist));
    }






    public ArtistResponseDTO updateArtist(int id, ArtistRequestDTO dto) {
        Artist artist = findArtist(id);
        String slug = requireSlug(dto.getName());
        assertSlugAvailable(slug, id);

        applyArtistFields(artist, dto, slug);
        artist.setGenres(resolveGenres(dto.getGenreIds()));

        return ResponseMapper.toArtistResponse(artistRepository.save(artist));
    }






    public List<ArtistResponseDTO> searchArtists(String name) {
        return artistRepository.findByNameContaining(name).stream().map(ResponseMapper::toArtistResponse).toList();
    }






    public List<FestivalResponseDTO> getFestivalsOfArtist(int artistId) {
        Artist artist = artistRepository.findWithFestivalsById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist with id " + artistId + " not found"));
        return artist.getFestivals().stream().map(ResponseMapper::toFestivalResponse).toList();
    }

    public boolean deleteArtist(int id) {
        if (!artistRepository.existsById(id)) {
            return false;
        }
        if (performanceRepository.existsByArtist_Id(id)) {
            throw new ResourceInUseException("Artist is referenced by a performance and cannot be deleted");
        }
        if (festivalRepository.existsByArtists_Id(id)) {
            throw new ResourceInUseException("Artist is referenced by a festival lineup and cannot be deleted");
        }
        if (userArtistFavoriteRepository.existsByArtistId(id)) {
            throw new ResourceInUseException("Artist is referenced by user favorites and cannot be deleted");
        }
        artistRepository.deleteById(id);
        return true;
    }




    private Artist findArtist(int id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist with id " + id + " not found"));
    }






    private void applyArtistFields(Artist artist, ArtistRequestDTO dto, String slug) {
        artist.setName(dto.getName().trim());
        artist.setSlug(slug);
        artist.setCountry(dto.getCountry().trim());
        artist.setImageUrl(dto.getImageUrl());
        artist.setSpotifyUrl(dto.getSpotifyUrl());
        artist.setInstagramUrl(dto.getInstagramUrl());
        artist.setSoundcloudUrl(dto.getSoundcloudUrl());
        artist.setYoutubeUrl(dto.getYoutubeUrl());
        artist.setBio(dto.getBio());
    }








    private String requireSlug(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("Artist name cannot be blank");
        }
        String slug = SlugNormalizer.fromName(name);
        if (slug.isBlank()) {
            throw new InvalidRequestException("Artist name does not produce a valid slug");
        }
        return slug;
    }








    private void assertSlugAvailable(String slug, Integer excludeId) {
        boolean taken = excludeId == null
                ? artistRepository.existsBySlug(slug)
                : artistRepository.existsBySlugAndIdNot(slug, excludeId);
        if (taken) {
            throw new DuplicateResourceException("Artist with normalized name '" + slug + "' already exists");
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

    private Page<ArtistResponseDTO> mapArtistsWithGenres(Page<Artist> page) {
        List<Artist> content = page.getContent();
        if (content.isEmpty()) {
            return page.map(ResponseMapper::toArtistResponse);
        }
        Map<Integer, Artist> withGenres = artistRepository.findWithGenresByIdIn(content.stream().map(Artist::getId).toList())
                .stream()
                .collect(Collectors.toMap(Artist::getId, Function.identity()));
        return page.map(artist -> ResponseMapper.toArtistResponse(withGenres.getOrDefault(artist.getId(), artist)));
    }
}
