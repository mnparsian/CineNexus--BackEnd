package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.media.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season,Long> {
    public Optional<Season> findBySeasonNumberAndMedia_TmdbId(int season,Long tmdbId);
    public List<Season> findByMedia(Media media);
}
