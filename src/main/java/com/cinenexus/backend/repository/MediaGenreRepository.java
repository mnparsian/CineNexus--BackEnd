package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.media.MediaGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaGenreRepository extends JpaRepository<MediaGenre,Long> {
    @Query("SELECT mg.media FROM MediaGenre mg WHERE mg.genre.id = :genreId")
    Page<Media> findMediaByGenreId(@Param("genreId") Long genreId, Pageable pageable);
}
