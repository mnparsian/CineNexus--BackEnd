package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    Optional<Genre> findByName(String name);
    Optional<Genre> findByTmdbId(Long tmdbId); // اضافه کردن جستجو بر اساس `tmdbId`

    @Query("SELECT g FROM Genre g WHERE g.tmdbId IN :tmdbIds")
    List<Genre> findAllByTmdbIdIn(@Param("tmdbIds") List<Integer> tmdbIds);

}
