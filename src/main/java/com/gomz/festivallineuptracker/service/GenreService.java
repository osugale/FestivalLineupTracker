package com.gomz.festivallineuptracker.service;

import com.gomz.festivallineuptracker.dto.ResponseMapper;
import com.gomz.festivallineuptracker.dto.GenreRelationRequestDTO;
import com.gomz.festivallineuptracker.dto.GenreRelationResponseDTO;
import com.gomz.festivallineuptracker.dto.GenreRequestDTO;
import com.gomz.festivallineuptracker.dto.GenreResponseDTO;
import com.gomz.festivallineuptracker.exception.DuplicateResourceException;
import com.gomz.festivallineuptracker.exception.InvalidRequestException;
import com.gomz.festivallineuptracker.exception.ResourceNotFoundException;
import com.gomz.festivallineuptracker.model.Genre;
import com.gomz.festivallineuptracker.model.GenreRelation;
import com.gomz.festivallineuptracker.repository.GenreRelationRepository;
import com.gomz.festivallineuptracker.repository.GenreRepository;
import com.gomz.festivallineuptracker.util.SlugNormalizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreRelationRepository genreRelationRepository;

    public GenreService(GenreRepository genreRepository, GenreRelationRepository genreRelationRepository) {
        this.genreRepository = genreRepository;
        this.genreRelationRepository = genreRelationRepository;
    }








    public List<GenreResponseDTO> getGenres() {
        return genreRepository.findAll().stream().map(ResponseMapper::toGenreResponse).toList();
    }





    public GenreResponseDTO getGenreById(int id) {
        return ResponseMapper.toGenreResponse(findGenre(id));
    }






    public GenreResponseDTO createGenre(GenreRequestDTO request) {
        String name = request.getName().trim();
        String slug = requireSlug(name);
        assertNameAndSlugAvailable(name, slug, null);

        Genre genre = new Genre();
        genre.setName(name);
        genre.setSlug(slug);
        return ResponseMapper.toGenreResponse(genreRepository.save(genre));
    }




    public GenreResponseDTO updateGenre(int id, GenreRequestDTO request) {
        Genre genre = findGenre(id);
        String name = request.getName().trim();
        String slug = requireSlug(name);
        assertNameAndSlugAvailable(name, slug, id);

        genre.setName(name);
        genre.setSlug(slug);
        return ResponseMapper.toGenreResponse(genreRepository.save(genre));
    }






    public void deleteGenre(int id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre with id " + id + " not found");
        }
        genreRelationRepository.deleteByParentGenre_Id(id);
        genreRelationRepository.deleteByChildGenre_Id(id);
        genreRepository.deleteById(id);
    }





    public List<GenreRelationResponseDTO> getRelationsForGenre(int genreId) {
        findGenre(genreId);
        return genreRelationRepository.findByParentGenre_Id(genreId).stream()
                .map(ResponseMapper::toGenreRelationResponse)
                .toList();
    }






    public GenreRelationResponseDTO createRelation(int parentGenreId, int childGenreId) {
        Genre parent = findGenre(parentGenreId);
        Genre child = findGenre(childGenreId);

        if (parent.getId() == child.getId()) {
            throw new InvalidRequestException("A genre cannot be its own child");
        }

        if (genreRelationRepository.existsByParentGenre_IdAndChildGenre_Id(parent.getId(), child.getId())) {
            throw new DuplicateResourceException("Genre taxonomy relation already exists");
        }

        if (hasPathFromChildToParent(child.getId(), parent.getId())) {
            throw new InvalidRequestException("Genre taxonomy cannot contain cycles");
        }

        GenreRelation relation = new GenreRelation(parent, child);

        return ResponseMapper.toGenreRelationResponse(
                genreRelationRepository.save(relation));
    }









    public void deleteRelation(int id) {
        if (!genreRelationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre relation with id " + id + " not found");
        }
        genreRelationRepository.deleteById(id);
    }








    private Genre findGenre(int id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre with id " + id + " not found"));
    }

    private boolean hasPathFromChildToParent(int childGenreId, int parentGenreId) {
        ArrayDeque<Integer> genresToVisit = new ArrayDeque<>();
        Set<Integer> visitedGenreIds = new HashSet<>();
        genresToVisit.add(childGenreId);

        while (!genresToVisit.isEmpty()) {
            int currentGenreId = genresToVisit.removeFirst();
            if (!visitedGenreIds.add(currentGenreId)) {
                continue;
            }
            if (currentGenreId == parentGenreId) {
                return true;
            }
            genreRelationRepository.findByParentGenre_Id(currentGenreId).stream()
                    .map(relation -> relation.getChildGenre().getId())
                    .forEach(genresToVisit::addLast);
        }
        return false;
    }






    private String requireSlug(String name) {
        String slug = SlugNormalizer.fromName(name);
        if (slug.isBlank()) {
            throw new InvalidRequestException("Genre name does not produce a valid slug");
        }
        return slug;
    }








    private void assertNameAndSlugAvailable(String name, String slug, Integer excludeId) {
        boolean nameTaken = excludeId == null
                ? genreRepository.existsByNameIgnoreCase(name)
                : genreRepository.existsByNameIgnoreCaseAndIdNot(name, excludeId);
        if (nameTaken) {
            throw new DuplicateResourceException("Genre with name '" + name + "' already exists");
        }

        boolean slugTaken = excludeId == null
                ? genreRepository.existsBySlug(slug)
                : genreRepository.existsBySlugAndIdNot(slug, excludeId);
        if (slugTaken) {
            throw new DuplicateResourceException("Genre with slug '" + slug + "' already exists");
        }
    }
}
