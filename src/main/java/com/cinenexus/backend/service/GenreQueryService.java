package com.cinenexus.backend.service;


import com.cinenexus.backend.model.media.Genre;
import com.cinenexus.backend.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreQueryService {

    private final GenreRepository genreRepository;

    public GenreQueryService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    public Optional<Genre> getGenreByTmdbId(Long tmdbId) {
        return genreRepository.findByTmdbId(tmdbId);
    }

    public Optional<Genre> getGenreByName(String name) {
        return genreRepository.findByName(name);
    }

    public List<Genre> getGenresByTmdbIds(List<Integer> tmdbIds) {
        return genreRepository.findAllByTmdbIdIn(tmdbIds);
    }
}

