package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.Episode;
import com.cinenexus.backend.model.media.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode,Long> {
    public List<Episode> findBySeasonId(Long seasonId);
}
