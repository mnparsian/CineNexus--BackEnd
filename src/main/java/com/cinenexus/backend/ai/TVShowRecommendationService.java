//package com.cinenexus.backend.ai;
//
//import com.cinenexus.backend.dto.media.MediaResponseDTO;
//import com.cinenexus.backend.model.media.*;
//import com.cinenexus.backend.model.user.User;
//import com.cinenexus.backend.repository.MediaRepository;
//import com.cinenexus.backend.repository.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class TVShowRecommendationService {
//    private final MediaRepository mediaRepository;
//    private final UserRepository userRepository;
//
//    public Page<MediaResponseDTO> getRecommendedTVShows(Long userId, Pageable pageable) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        Set<String> favoriteGenres = user.getFavoriteMovies().stream()
//                .map(movie -> movie.getMedia().getMediaGenres())
//                .flatMap(Collection::stream)
//                .map(Genre->Genre.getGenre().getName())
//                .distinct()
//                .collect(Collectors.toSet();
//
//        Set<String> favoriteDirectors = user.getFavoriteMovies().stream()
//                .map(movie -> movie.getMedia().getMediaCrew())
//                .flatMap(Collection::stream)
//                .filter(crew -> crew.getCrewRole().getName().equalsIgnoreCase("Directing"))
//                .map(crew -> crew.getPerson().getName())
//                .distinct()
//                .collect(Collectors.toSet();
//
//        Set<String> favoriteActors = user.getFavoriteMovies().stream()
//                .map(movie -> movie.getMedia().getMediaCrew())
//                .flatMap(Collection::stream)
//                .map(cast -> cast.getPerson().getName())
//                .distinct()
//                .collect(Collectors.toSet();
//
//        Set<String> favoriteCompanies = user.getFavoriteMovies().stream()
//                .map(movie -> movie.getMedia().getProductionCompanies())
//                .flatMap(Collection::stream)
//                .map(ProductionCompany::getName)
//                .distinct()
//                .collect(Collectors.toSet();
//
//        Set<Long> favoriteMovieIds = user.getFavoriteMovies().stream()
//                .map(FavoriteMovie::getId)
//                .collect(Collectors.toSet();
//
//        Set<Media> recommendedTVShows = mediaRepository.findRecommendedMovies(
//                favoriteGenres, favoriteDirectors, favoriteActors, favoriteCompanies, favoriteMovieIds, pageable);
//
//        return recommendedTVShows.map(MediaResponseDTO::fromEntity);
//    }
//}
//
