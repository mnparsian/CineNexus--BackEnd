package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.Media;
import jdk.jfr.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {
    boolean existsByTmdbId(Long tmdbId);
    Optional<Media> findByTmdbId(Long tmdbId);
    List<Media> findByIsTVShowTrue();
    Page<Media> findByCategory(String category,Pageable pageable);
    Page<Media> findByTitleContainingIgnoreCase(String title, Pageable page);
    Page<Media> findByMediaType_Name(String name,Pageable pageable);
    Page<Media> findAllByOrderByPopularityDesc(Pageable pageable);

}
