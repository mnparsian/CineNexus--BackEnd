package com.cinenexus.backend.service;

import com.cinenexus.backend.dto.favoriteMovie.FavoriteMovieResponseDTO;
import com.cinenexus.backend.model.media.FavoriteMovie;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.FavoriteMovieRepository;
import com.cinenexus.backend.repository.MediaRepository;
import com.cinenexus.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteMovieService {

    private final FavoriteMovieRepository favoriteMovieRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final FavoriteMovieResponseDTO favoriteMovieResponseDTO;


    @Transactional
    public void addFavoriteMovie(Long userId, Long tmdbId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Media media = mediaRepository.findByTmdbId(tmdbId)
                .orElseThrow(() -> new EntityNotFoundException("Media not found"));


        if (favoriteMovieRepository.existsByUserAndMedia(user, media)) {
            throw new IllegalStateException("Movie is already in favorites!");
        }

        FavoriteMovie favoriteMovie = new FavoriteMovie(user, media);
        favoriteMovieRepository.save(favoriteMovie);
    }


    @Transactional
    public void removeFavoriteMovie(Long userId, Long tmdbId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Media media = mediaRepository.findByTmdbId(tmdbId)
                .orElseThrow(() -> new EntityNotFoundException("Media not found"));

        FavoriteMovie favoriteMovie = favoriteMovieRepository.findByUserAndMedia(user, media)
                .orElseThrow(() -> new EntityNotFoundException("Movie is not in favorites!"));

        favoriteMovieRepository.delete(favoriteMovie);
    }


    public boolean isFavoriteMovie(Long userId, Long tmdbId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Media media = mediaRepository.findByTmdbId(tmdbId)
                .orElseThrow(() -> new EntityNotFoundException("Media not found"));

        return favoriteMovieRepository.existsByUserAndMedia(user, media);
    }

    public List<FavoriteMovieResponseDTO> getFavoriteMovies(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<FavoriteMovie> favoriteMovies = favoriteMovieRepository.findByUser(user);
        return favoriteMovies.stream().map(favoriteMovieResponseDTO::toDto).toList();

    }
}
