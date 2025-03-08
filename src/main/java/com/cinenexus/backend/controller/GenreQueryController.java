package com.cinenexus.backend.controller;


import com.cinenexus.backend.model.media.Genre;
import com.cinenexus.backend.service.GenreQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres-query")
public class GenreQueryController {

  private final GenreQueryService genreQueryService;

  public GenreQueryController(GenreQueryService genreQueryService) {
    this.genreQueryService = genreQueryService;
  }

  @GetMapping
  public ResponseEntity<List<Genre>> getAllGenres() {
    return ResponseEntity.ok(genreQueryService.getAllGenres());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getGenreById(@PathVariable Long id) {
    return genreQueryService
        .getGenreById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/tmdb/{tmdbId}")
  public ResponseEntity<?> getGenreByTmdbId(@PathVariable Long tmdbId) {
    return genreQueryService
        .getGenreByTmdbId(tmdbId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/tmdb-ids")
  public ResponseEntity<List<Genre>> getGenresByTmdbIds(@RequestBody List<Integer> tmdbIds) {
    List<Genre> genres = genreQueryService.getGenresByTmdbIds(tmdbIds);
    return ResponseEntity.ok(genres);
  }
    }

