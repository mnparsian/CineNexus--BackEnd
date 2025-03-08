package com.cinenexus.backend.controller;

import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.service.MediaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/media-query")
@RequiredArgsConstructor
public class MediaQueryController {

    private final MediaQueryService mediaQueryService;

    @GetMapping
    public Page<Media> filterMedia(
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> category,
            @RequestParam Optional<String> mediaType,
            @RequestParam Optional<Double> minVoteAverage,
            @RequestParam Optional<Double> maxVoteAverage,
            @RequestParam Optional<LocalDate> releasedAfter,
            @RequestParam Optional<LocalDate> releasedBefore,
            Pageable pageable
    ) {
        return mediaQueryService.filterMedia(
                title, category, mediaType, minVoteAverage, maxVoteAverage, releasedAfter, releasedBefore, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable Long id) {
        return ResponseEntity.ok(mediaQueryService.getMediaById(id));
    }

    @GetMapping("/category")
    public ResponseEntity<Page<Media>> getMediaByCategory(@RequestParam String category,@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(mediaQueryService.getMediaByCategory(category,page,size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Media>> searchMediaByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(mediaQueryService.searchMediaByTitle(title, page, size));
    }

    @GetMapping("/popular")
    public ResponseEntity<Page<Media>> getPopularMedia(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(mediaQueryService.getMediaByPopularity(page, size));
    }
}

