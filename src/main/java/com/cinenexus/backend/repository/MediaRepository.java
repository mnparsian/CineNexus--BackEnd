package com.cinenexus.backend.repository;

import com.cinenexus.backend.enumeration.WatchlistStatusType;
import com.cinenexus.backend.model.media.Genre;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.user.User;
import jdk.jfr.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {
    boolean existsByTmdbId(Long tmdbId);
    Optional<Media> findByTmdbId(Long tmdbId);
    List<Media> findByIsTVShowTrue();
    Page<Media> findByCategory(String category,Pageable pageable);
    Page<Media> findByTitleContainingIgnoreCase(String title, Pageable page);
    Page<Media> findByMediaType_Name(String name,Pageable pageable);
    Page<Media> findAllByOrderByPopularityDesc(Pageable pageable);


    @Query("SELECT DISTINCT m FROM Media m " +
            "JOIN m.mediaGenres mg " +
            "JOIN mg.genre g " +
            "LEFT JOIN m.mediaCrew mc " +
            "LEFT JOIN mc.person p " +
            "LEFT JOIN m.productionCompanies pc " +
            "WHERE (g.id IN :favoriteGenres " +
            "OR p.id IN :favoriteDirectors " +
            "OR p.id IN :favoriteActors " +
            "OR pc.id IN :favoriteCompanies) " +
            "AND m.id NOT IN :watchedMovies " +
            "ORDER BY m.voteAverage DESC, m.popularity DESC")
    Page<Media> findRecommendedMovies(
            @Param("favoriteGenres") Set<Long> favoriteGenres,
            @Param("favoriteDirectors") Set<Long> favoriteDirectors,
            @Param("favoriteActors") Set<Long> favoriteActors,
            @Param("favoriteCompanies") Set<Long> favoriteCompanies,
            @Param("watchedMovies") List<Long> watchedMovies,
            Pageable pageable);







}

