package com.cinenexus.backend.ai;

import com.cinenexus.backend.ai.RecommendationService;
import com.cinenexus.backend.dto.media.MediaResponseDTO;
import com.cinenexus.backend.service.OpenAIService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;
//    private final TVShowRecommendationService tvShowRecommendationService;
    private final MovieRecommendationService movieRecommendationService;
     private final OpenAIService openAIService;

    public RecommendationController(RecommendationService recommendationService,MovieRecommendationService movieRecommendationService,OpenAIService openAIService) {
        this.recommendationService = recommendationService;
        this.movieRecommendationService = movieRecommendationService;
        this.openAIService = openAIService;
    }

    @GetMapping
    public ResponseEntity<Page<MediaResponseDTO>> getRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam String category) {

        Page<MediaResponseDTO> recommendedMedia = recommendationService.getRecommendedMedia(page, size, sortBy, category);
        return ResponseEntity.ok(recommendedMedia);
    }

    @GetMapping("/movies")
    public ResponseEntity<Page<MediaResponseDTO>> getRecommendedMovies(
            @RequestParam Long userId,
            @PageableDefault(size = 10, sort = "popularity", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MediaResponseDTO> recommendations = movieRecommendationService.getRecommendedMovies(userId, pageable);
        return ResponseEntity.ok(recommendations);
    }

//    @GetMapping("/tvshows")
//    public ResponseEntity<Page<MediaResponseDTO>> getRecommendedTVShows(
//            @RequestParam Long userId,
//            @PageableDefault(size = 10, sort = "popularity", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<MediaResponseDTO> recommendations = tvShowRecommendationService.getRecommendedTVShows(userId, pageable);
//        return ResponseEntity.ok(recommendations);
//    }


    @PostMapping("/ai")
    public ResponseEntity<String> getAIRecommendations(@RequestBody List<String> likedMovies) {
        String aiResponse = openAIService.getMovieRecommendations(likedMovies);
        return ResponseEntity.ok(aiResponse);
    }
}
