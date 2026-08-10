package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ArtistResponseDTO;
import com.gomz.festivallineuptracker.dto.FestivalRequestDTO;
import com.gomz.festivallineuptracker.dto.FestivalResponseDTO;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.repository.ArtistRepository;
import com.gomz.festivallineuptracker.repository.FestivalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestivalService {

    private final FestivalRepository festivalRepository;

    private ArtistRepository artistRepository;

    public FestivalService(FestivalRepository festivalRepository, ArtistRepository artistRepository) {
        this.festivalRepository = festivalRepository;
        this.artistRepository = artistRepository;
    }







    public Page<FestivalResponseDTO> getFestivals(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return festivalRepository.findAll(pageable).map(festival -> new FestivalResponseDTO(
                festival.getId(), festival.getName(), festival.getCity(), festival.getCountry(), festival.getVenue(), festival.getStartDate(), festival.getEndDate(), festival.getDescription(),
                festival.getImageUrl(), festival.getOfficialWebsite(), festival.getGenre()
        ));
    }









    public FestivalResponseDTO getFestivalById(int id) {

        Festival festival = festivalRepository.findById(id).orElse(null);

        if (festival == null) {
            throw new ResourceNotFoundException("Artist with id " + id + " not found");
        }

        return new FestivalResponseDTO(
                festival.getId(), festival.getName(), festival.getCity(), festival.getCountry(), festival.getVenue(), festival.getStartDate(), festival.getEndDate(), festival.getDescription(),
                festival.getImageUrl(), festival.getOfficialWebsite(), festival.getGenre()
        );
    }


    public List<FestivalResponseDTO> searchFestivals(String name) {

        return festivalRepository.findByNameContaining(name).stream().map(festival -> new FestivalResponseDTO(

                festival.getId(), festival.getName(), festival.getCity(), festival.getCountry(), festival.getVenue(), festival.getStartDate(), festival.getEndDate(),
                festival.getDescription(), festival.getImageUrl(), festival.getOfficialWebsite(), festival.getGenre()
        )).toList();


    }










    public FestivalResponseDTO addFestival(FestivalRequestDTO dto) {

        Festival festival = new Festival();

        festival.setName(dto.getName());
        festival.setCity(dto.getCity());
        festival.setCountry(dto.getCountry());
        festival.setVenue(dto.getVenue());
        festival.setStartDate(dto.getStartDate());
        festival.setEndDate(dto.getEndDate());
        festival.setDescription(dto.getDescription());
        festival.setImageUrl(dto.getImageUrl());
        festival.setOfficialWebsite(dto.getOfficialWebsite());
        festival.setGenre(dto.getGenre());

        Festival savedFestival = festivalRepository.save(festival);

        return new FestivalResponseDTO(
                savedFestival.getId(), savedFestival.getName(), savedFestival.getCity(), savedFestival.getCountry(), savedFestival.getVenue(), savedFestival.getStartDate(),
                savedFestival.getEndDate(), savedFestival.getDescription(), savedFestival.getImageUrl(), savedFestival.getOfficialWebsite(), savedFestival.getGenre()
        );
    }




    public FestivalResponseDTO addArtistToFestival(int festivalId, int artistId) {

        Festival festival = festivalRepository.findById(festivalId).orElse(null);
        Artist artist = artistRepository.findById(artistId).orElse(null);

        if (festival == null || artist == null) {
            throw new ResourceNotFoundException("Either Festival or Artist with id " + festivalId + artistId + " not found");
        }

        festival.getArtists().add(artist);

        Festival savedFestival = festivalRepository.save(festival);

        return new FestivalResponseDTO(
                savedFestival.getId(), savedFestival.getName(), savedFestival.getCity(), savedFestival.getCountry(), savedFestival.getVenue(), savedFestival.getStartDate(),
                savedFestival.getEndDate(), savedFestival.getDescription(), savedFestival.getImageUrl(), savedFestival.getOfficialWebsite(), savedFestival.getGenre()
        );
    }











    public FestivalResponseDTO updateFestival(int id, FestivalRequestDTO dto) {

        Festival festival = festivalRepository.findById(id).orElse(null);

        if (festival == null) {
            throw new ResourceNotFoundException("Festival with id " + id + " not found");
        }

        festival.setName(dto.getName());
        festival.setCity(dto.getCity());
        festival.setCountry(dto.getCountry());
        festival.setVenue(dto.getVenue());
        festival.setStartDate(dto.getStartDate());
        festival.setEndDate(dto.getEndDate());
        festival.setDescription(dto.getDescription());
        festival.setImageUrl(dto.getImageUrl());
        festival.setOfficialWebsite(dto.getOfficialWebsite());
        festival.setGenre(dto.getGenre());

        Festival updatedFestival = festivalRepository.save(festival);

        return new FestivalResponseDTO(
                updatedFestival.getId(), updatedFestival.getName(), updatedFestival.getCity(), updatedFestival.getCountry(), updatedFestival.getVenue(),
                updatedFestival.getStartDate(), updatedFestival.getEndDate(), updatedFestival.getDescription(), updatedFestival.getImageUrl(), updatedFestival.getOfficialWebsite(),
                updatedFestival.getGenre()
        );
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

        Festival festival = festivalRepository.findById(festivalId).orElse(null);

        if (festival == null) {
            throw new ResourceNotFoundException("Festival with id " + festivalId + " not found");
        }

        return festival.getArtists().stream().map(artist -> new ArtistResponseDTO(
                artist.getName(), artist.getGenre(), artist.getCountry(), artist.getImageUrl(), artist.getSpotifyUrl(), artist.getInstagramUrl(), artist.getSoundcloudUrl(),
                artist.getYoutubeUrl(), artist.getBio(), artist.getId()
        )).toList();

    }












    public boolean deleteFestival(int id) {

        if (!festivalRepository.existsById(id)) {
            return false;
        }

        festivalRepository.deleteById(id);

        return true;
    }
}