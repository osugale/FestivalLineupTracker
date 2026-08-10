package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ArtistRequestDTO;
import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }








    public Page<ArtistResponseDTO> getArtists(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return artistRepository.findAll(pageable).map(artist -> new ArtistResponseDTO(artist.getName(), artist.getGenre(), artist.getCountry(),
                artist.getImageUrl(), artist.getSpotifyUrl(), artist.getInstagramUrl(), artist.getSoundcloudUrl(), artist.getYoutubeUrl(),
                artist.getBio(), artist.getId()));

    }








    public ArtistResponseDTO getArtistById(int id) {

        Artist artist = artistRepository.findById(id).orElse(null);

        if (artist == null) {
            throw new ResourceNotFoundException("Artist with id " + id + " not found");
        }

        return new ArtistResponseDTO(
                artist.getName(), artist.getGenre(), artist.getCountry(),
                artist.getImageUrl(), artist.getSpotifyUrl(), artist.getInstagramUrl(), artist.getSoundcloudUrl(), artist.getYoutubeUrl(),
                artist.getBio(), artist.getId()
        );

    }












    public ArtistResponseDTO addArtist(ArtistRequestDTO artistDTO) {

        Artist artist = new Artist();

        artist.setName(artistDTO.getName());
        artist.setGenre(artistDTO.getGenre());
        artist.setCountry(artistDTO.getCountry());
        artist.setImageUrl(artistDTO.getImageUrl());
        artist.setSpotifyUrl(artistDTO.getSpotifyUrl());
        artist.setInstagramUrl(artistDTO.getInstagramUrl());
        artist.setSoundcloudUrl(artistDTO.getSoundcloudUrl());
        artist.setYoutubeUrl(artistDTO.getYoutubeUrl());
        artist.setBio(artistDTO.getBio());

        Artist savedArtist = artistRepository.save(artist);

        return new ArtistResponseDTO(
                savedArtist.getName(), savedArtist.getGenre(), savedArtist.getCountry(), savedArtist.getImageUrl(), savedArtist.getSpotifyUrl(),
                savedArtist.getInstagramUrl(), savedArtist.getSoundcloudUrl(), savedArtist.getYoutubeUrl(), savedArtist.getBio(), savedArtist.getId()
        );
    }










    public ArtistResponseDTO updateArtist(int id, ArtistRequestDTO dto) {

        Artist artist = artistRepository.findById(id).orElse(null);

        if (artist == null) {
            throw new ResourceNotFoundException("Artist with id " + id + " not found");
        }

        artist.setName(dto.getName());
        artist.setGenre(dto.getGenre());
        artist.setCountry(dto.getCountry());
        artist.setImageUrl(dto.getImageUrl());
        artist.setSpotifyUrl(dto.getSpotifyUrl());
        artist.setInstagramUrl(dto.getInstagramUrl());
        artist.setSoundcloudUrl(dto.getSoundcloudUrl());
        artist.setYoutubeUrl(dto.getYoutubeUrl());
        artist.setBio(dto.getBio());

        Artist updatedArtist = artistRepository.save(artist);

        return new ArtistResponseDTO(
                updatedArtist.getName(), updatedArtist.getGenre(), updatedArtist.getCountry(), updatedArtist.getImageUrl(), updatedArtist.getSpotifyUrl(),
                updatedArtist.getInstagramUrl(), updatedArtist.getSoundcloudUrl(), updatedArtist.getYoutubeUrl(), updatedArtist.getBio(), updatedArtist.getId()
        );
    }










    public List<ArtistResponseDTO> searchArtists(String name) {

        return artistRepository.findByNameContaining(name).stream().map(artist -> new ArtistResponseDTO(
                artist.getName(), artist.getGenre(), artist.getCountry(),
                artist.getImageUrl(), artist.getSpotifyUrl(), artist.getInstagramUrl(), artist.getSoundcloudUrl(), artist.getYoutubeUrl(),
                artist.getBio(), artist.getId())).toList();
    }









    public List<FestivalResponseDTO> getFestivalsOfArtist(int artistId) {

        Artist artist = artistRepository.findById(artistId).orElse(null);

        if (artist == null) {
            throw new ResourceNotFoundException("Artist with id " + artistId + " not found");
        }

        return artist.getFestivals().stream().map(festival -> new FestivalResponseDTO
                (festival.getId(), festival.getName(), festival.getCity(), festival.getCountry(), festival.getVenue(), festival.getStartDate(), festival.getEndDate(), festival.getDescription(), festival.getImageUrl(),
                festival.getOfficialWebsite(),
                festival.getGenre()
        )).toList();
    }









    public boolean deleteArtist(int id) {

        if (!artistRepository.existsById(id)) {
            return false;
        }

        artistRepository.deleteById(id);
        return true;
    }
}