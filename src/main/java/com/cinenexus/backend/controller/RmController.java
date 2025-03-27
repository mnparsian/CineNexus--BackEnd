package com.cinenexus.backend.controller;



import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.service.RmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/rm")
public class RmController {

    private final RmService rmService;

    public RmController(RmService rmService) {
        this.rmService = rmService;
    }

    @GetMapping("/recommended")
    public ResponseEntity<Page<Media>> getRecommendedMovies(
            @RequestParam(required = false) Set<Long> favoriteGenres,
            @RequestParam(required = false) Set<Long> favoriteDirectors,
            @RequestParam(required = false) Set<Long> favoriteActors,
            @RequestParam(required = false) Set<Long> favoriteCompanies,
            @RequestParam(required = false) List<Long> watchedMovies,
            Pageable pageable) {

        Page<Media> recommendedMovies = rmService.findRecommendedMovies(
                favoriteGenres != null ? favoriteGenres : Set.of(-1L),
                favoriteDirectors != null ? favoriteDirectors : Set.of(-1L),
                favoriteActors != null ? favoriteActors : Set.of(-1L),
                favoriteCompanies != null ? favoriteCompanies : Set.of(-1L),
                watchedMovies != null ? watchedMovies : List.of(-1L),
                pageable);

        return ResponseEntity.ok(recommendedMovies);
    }
}
