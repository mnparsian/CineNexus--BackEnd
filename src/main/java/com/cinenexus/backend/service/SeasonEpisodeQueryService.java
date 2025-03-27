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


    public List<Season> getSeasonsByMediaId(Long mediaId) {
        return seasonRepository.findByMedia(new Media(mediaId));
    }


    public Optional<Season> getSeasonById(Long seasonId) {
        return seasonRepository.findById(seasonId);
    }


    public Optional<Season> getSeasonByNumberAndMediaTmdbId(int seasonNumber, Long tmdbId) {
        return seasonRepository.findBySeasonNumberAndMedia_TmdbId(seasonNumber, tmdbId);
    }


    public List<Episode> getEpisodesBySeasonId(Long seasonId) {
        return episodeRepository.findBySeasonId(seasonId);
    }


    public Optional<Episode> getEpisodeById(Long episodeId) {
        return episodeRepository.findById(episodeId);
    }
}
