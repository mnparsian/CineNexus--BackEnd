package com.cinenexus.backend.controller;


import com.cinenexus.backend.model.media.Genre;
import com.cinenexus.backend.service.GenreService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

  private final GenreService genreService;

  public GenreController(GenreService genreService) {
    this.genreService = genreService;
  }

  // دریافت و ذخیره ژانرهای فیلم
  @PostMapping("/movies")
  public ResponseEntity<String> fetchMovieGenres() {
    genreService.fetchAndStoreGenres("movie");
    return ResponseEntity.ok("Movie genres fetched and stored successfully.");
  }

  // دریافت و ذخیره ژانرهای سریال
  @PostMapping("/tv")
  public ResponseEntity<String> fetchTVGenres() {
    genreService.fetchAndStoreGenres("tv");
    return ResponseEntity.ok("TV show genres fetched and stored successfully.");
  }

  @GetMapping
  public ResponseEntity<Page<Genre>> getAllGenres(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "name") String sortBy,
          @RequestParam(defaultValue = "asc") String direction) {
    return ResponseEntity.ok(genreService.getAllGenres(page, size, sortBy, direction));
  }
}
