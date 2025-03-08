package com.cinenexus.backend.controller;

import com.cinenexus.backend.model.media.*;
import com.cinenexus.backend.service.SeasonEpisodeQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons-episodes")
public class SeasonEpisodeQueryController {

    private final SeasonEpisodeQueryService seasonEpisodeQueryService;

    public SeasonEpisodeQueryController(SeasonEpisodeQueryService seasonEpisodeQueryService) {
        this.seasonEpisodeQueryService = seasonEpisodeQueryService;
    }

    @GetMapping("/seasons/media/{mediaId}")
    public ResponseEntity<List<Season>> getSeasonsByMediaId(@PathVariable Long mediaId) {
        return ResponseEntity.ok(seasonEpisodeQueryService.getSeasonsByMediaId(mediaId));
    }

    @GetMapping("/season/{seasonId}")
    public ResponseEntity<?> getSeasonById(@PathVariable Long seasonId) {
        return seasonEpisodeQueryService.getSeasonById(seasonId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/season/{tmdbId}/{seasonNumber}")
    public ResponseEntity<?> getSeasonByNumberAndMediaTmdbId(@PathVariable int seasonNumber, @PathVariable Long tmdbId) {
        return seasonEpisodeQueryService.getSeasonByNumberAndMediaTmdbId(seasonNumber, tmdbId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/episodes/season/{seasonId}")
    public ResponseEntity<List<Episode>> getEpisodesBySeasonId(@PathVariable Long seasonId) {
        return ResponseEntity.ok(seasonEpisodeQueryService.getEpisodesBySeasonId(seasonId));
    }

    @GetMapping("/episode/{episodeId}")
    public ResponseEntity<?> getEpisodeById(@PathVariable Long episodeId) {
        return seasonEpisodeQueryService.getEpisodeById(episodeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
