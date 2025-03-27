package com.cinenexus.backend.ai;



import com.cinenexus.backend.dto.media.MediaResponseDTO;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.media.MediaCrew;
import com.cinenexus.backend.model.media.ProductionCompany;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.MediaRepository;
import com.cinenexus.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieRecommendationService {

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final MediaResponseDTO mediaResponseDTO;


    public Page<MediaResponseDTO> getRecommendedMovies(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Media> favoriteMovies = user.getFavoriteMovies().stream()
                .map(fm -> fm.getMedia())
                .collect(Collectors.toSet());

        if (favoriteMovies.isEmpty()) {
            return Page.empty();
        }

        Set<Long> favoriteGenres = favoriteMovies.stream()
                .flatMap(movie -> movie.getMediaGenres().stream().map(genre -> genre.getId()))
                .collect(Collectors.toSet());

        Set<Long> favoriteDirectors = favoriteMovies.stream()
                .flatMap(movie -> movie.getMediaCrew().stream()
                        .filter(mc -> mc.getCrewRole().getName().equalsIgnoreCase("Writing"))
                        .map(mc -> mc.getPerson().getId()))
                .collect(Collectors.toSet());

        Set<Long> favoriteActors = favoriteMovies.stream()
                .flatMap(movie -> movie.getMediaCrew().stream()
                        .filter(mc -> mc.getCrewRole().getName().equalsIgnoreCase("Acting"))
                        .map(mc -> mc.getPerson().getId()))
                .collect(Collectors.toSet());

        Set<Long> favoriteCompanies = favoriteMovies.stream()
                .flatMap(movie -> movie.getProductionCompanies().stream().map(ProductionCompany::getId))
                .collect(Collectors.toSet());

        List<Long> favoriteMovieIds = favoriteMovies.stream()
                .map(Media::getId)
                .collect(Collectors.toList());

//        if (favoriteGenres.isEmpty()) favoriteGenres = Set.of(-1L);
//        if (favoriteDirectors.isEmpty()) favoriteDirectors = Set.of(-1L);
//        if (favoriteActors.isEmpty()) favoriteActors = Set.of(-1L);
//        if (favoriteCompanies.isEmpty()) favoriteCompanies = Set.of(-1L);
//        if (favoriteMovieIds.isEmpty()) favoriteMovieIds = List.of(-1L);
//
//        System.out.println("Favorite Genres: {}"+ favoriteGenres);
//        System.out.println("Favorite Directors: {}"+ favoriteDirectors);
//        System.out.println("Favorite Actors: {}"+ favoriteActors);
//        System.out.println("Favorite Companies: {}"+ favoriteCompanies);
//        System.out.println("Watched Movies: {}"+ favoriteMovieIds);
//        if (favoriteDirectors.isEmpty()) favoriteDirectors = null;
//        if (favoriteActors.isEmpty()) favoriteActors = null;
//        if (favoriteMovieIds.isEmpty()) favoriteMovieIds = List.of(-1L);



        Page<Media> recommendedMovies = mediaRepository.findRecommendedMovies(
                favoriteGenres, favoriteDirectors, favoriteActors, favoriteCompanies, favoriteMovieIds, pageable);



        return recommendedMovies.map(mediaResponseDTO::fromEntity);
    }
}

