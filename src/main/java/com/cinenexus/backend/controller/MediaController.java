package com.cinenexus.backend.controller;

import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.service.MediaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/media")
public class MediaController {
  private final MediaService mediaService;
  @Value("${tmdb.api.key}")
  private  String TMDB_API_KEY ;
  public MediaController(MediaService mediaService) {
    this.mediaService = mediaService;
  }
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/fetch/movie/{tmdbId}")
  public ResponseEntity<Media> fetchAndStoreMovie(@PathVariable Long tmdbId) {
    return ResponseEntity.ok(mediaService.fetchAndStoreMovie(tmdbId));
  }
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/fetch/tv/{tmdbId}")
  public ResponseEntity<Media> fetchAndStoreTVShow(@PathVariable Long tmdbId) {
    return ResponseEntity.ok(mediaService.fetchAndStoreTVShow(tmdbId));
  }

  @GetMapping("/movies/popular")
  public ResponseEntity<Page<Media>> getPopularMovies(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size) {
    return ResponseEntity.ok(mediaService.fetchPopularMovies(page, size));
  }

  @GetMapping("/tv/popular")
  public ResponseEntity<Page<Media>> getPopularTVShows(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size) {
    return ResponseEntity.ok(mediaService.fetchPopularTVShows(page, size));
  }

  @GetMapping("/movies/now_playing")
  public ResponseEntity<Page<Media>> getNowPlayingMovies(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size) {
    return ResponseEntity.ok(mediaService.fetchNowPlayingMovies(page, size));
  }

  @GetMapping("/tv/on_the_air")
  public ResponseEntity<Page<Media>> getAiringTVShows(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size) {
    return ResponseEntity.ok(mediaService.fetchNowAiringTVShows(page, size));
  }
  @GetMapping("/movies/top-rated")
  public ResponseEntity<Page<Media>> getTopRatedMovies(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(mediaService.fetchTopRatedMovies(page, size));
  }

  @GetMapping("/tv/top-rated")
  public ResponseEntity<Page<Media>> getTopRatedTVShows(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(mediaService.fetchTopRatedTVShows(page, size));
  }
  @GetMapping("/search")
  public ResponseEntity<Page<Media>> searchMedia(
          @RequestParam String query,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(mediaService.searchMedia(query, page, size));
  }

  @GetMapping("/{tmdbId}/credits")
  public ResponseEntity<Page<Media>> getMediaCredits(
          @PathVariable Long tmdbId,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "false") boolean isTVShow) {

    String url = "https://api.themoviedb.org/3/movie/" + tmdbId + "/credits?api_key=" + TMDB_API_KEY;

    return ResponseEntity.ok(mediaService.fetchMediaCredits(url, page, size, isTVShow));
  }
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/update-tv-details")
  public ResponseEntity<String> updateTVShowDetails() {
    mediaService.fetchAndSaveTVDetails();
    return ResponseEntity.ok("Updating of series details has begun.");
  }

}
