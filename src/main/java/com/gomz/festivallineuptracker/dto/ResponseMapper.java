package com.gomz.festivallineuptracker.dto;

import com.gomz.festivallineuptracker.model.Artist;
import com.gomz.festivallineuptracker.model.Festival;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.model.GenreRelation;

import java.util.Comparator;
import java.util.List;

public final class ResponseMapper {





    private ResponseMapper() {
    }











    public static List<GenreResponseDTO> toGenreResponses(Iterable<Genre> genres) {
        List<GenreResponseDTO> responses = new java.util.ArrayList<>();
        for (Genre genre : genres) {responses.add(toGenreResponse(genre));}
        responses.sort(Comparator.comparing(GenreResponseDTO::getName, String.CASE_INSENSITIVE_ORDER));
        return responses;
    }






    public static GenreRelationResponseDTO toGenreRelationResponse(GenreRelation relation) {
        return new GenreRelationResponseDTO(relation.getId(), toGenreResponse(relation.getParentGenre()), toGenreResponse(relation.getChildGenre())
        );
    }









    //responseDTOs

    public static GenreResponseDTO toGenreResponse(Genre genre) {
        return new GenreResponseDTO(genre.getId(), genre.getName(), genre.getSlug());
    }





    public static ArtistResponseDTO toArtistResponse(Artist artist) {
        return new ArtistResponseDTO(artist.getId(), artist.getName(), artist.getSlug(), artist.getCountry(), artist.getImageUrl(),artist.getSpotifyUrl(), artist.getInstagramUrl(),
                artist.getSoundcloudUrl(), artist.getYoutubeUrl(), artist.getBio(), toGenreResponses(artist.getGenres()));
    }





    public static FestivalResponseDTO toFestivalResponse(Festival festival) {
        return new FestivalResponseDTO(festival.getId(), festival.getName(), festival.getCity(), festival.getCountry(), festival.getVenue(), festival.getStartDate(),
                festival.getEndDate(), festival.getTimezone(), festival.getDescription(), festival.getImageUrl(), festival.getOfficialWebsite(), festival.getTimetableUrl(),
                toGenreResponses(festival.getGenres()));
    }
}
