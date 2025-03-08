package com.cinenexus.backend.service;

import com.cinenexus.backend.model.media.*;
import com.cinenexus.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonEpisodeQueryService {

    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;

    public SeasonEpisodeQueryService(SeasonRepository seasonRepository, EpisodeRepository episodeRepository) {
        this.seasonRepository = seasonRepository;
        this.episodeRepository = episodeRepository;
    }

    // دریافت تمام فصل‌های یک سریال بر اساس ID
    public List<Season> getSeasonsByMediaId(Long mediaId) {
        return seasonRepository.findByMedia(new Media(mediaId));
    }

    // دریافت یک فصل بر اساس ID
    public Optional<Season> getSeasonById(Long seasonId) {
        return seasonRepository.findById(seasonId);
    }

    // دریافت یک فصل بر اساس شماره فصل و TMDB ID سریال
    public Optional<Season> getSeasonByNumberAndMediaTmdbId(int seasonNumber, Long tmdbId) {
        return seasonRepository.findBySeasonNumberAndMedia_TmdbId(seasonNumber, tmdbId);
    }

    // دریافت تمام اپیزودهای یک فصل خاص بهینه شده با کوئری در دیتابیس
    public List<Episode> getEpisodesBySeasonId(Long seasonId) {
        return episodeRepository.findBySeasonId(seasonId);
    }

    // دریافت یک اپیزود مشخص بر اساس ID
    public Optional<Episode> getEpisodeById(Long episodeId) {
        return episodeRepository.findById(episodeId);
    }
}
