package com.cinenexus.backend.controller;

import com.cinenexus.backend.dto.favoriteMovie.FavoriteMovieResponseDTO;
import com.cinenexus.backend.service.FavoriteMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteMovieController {

    private final FavoriteMovieService favoriteMovieService;


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<String> addFavoriteMovie(@RequestParam Long userId, @RequestParam Long tmdbId) {
        favoriteMovieService.addFavoriteMovie(userId, tmdbId);
        return ResponseEntity.ok("Movie added to favorites successfully!");
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping
    public ResponseEntity<String> removeFavoriteMovie(@RequestParam Long userId, @RequestParam Long tmdbId) {
        favoriteMovieService.removeFavoriteMovie(userId, tmdbId);
        return ResponseEntity.ok("Movie removed from favorites successfully!");
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFavorite(@RequestParam Long userId, @RequestParam Long tmdbId) {
        boolean isFavorite = favoriteMovieService.isFavoriteMovie(userId, tmdbId);
        return ResponseEntity.ok(isFavorite);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<FavoriteMovieResponseDTO>> getFavoriteMovies(@RequestParam Long userId) {
        List<FavoriteMovieResponseDTO> favoriteMovies = favoriteMovieService.getFavoriteMovies(userId);
        return ResponseEntity.ok(favoriteMovies);
    }
}
