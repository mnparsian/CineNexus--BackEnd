package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.FavoriteMovie;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {
    List<FavoriteMovie> findByUser(User user);
    Optional<FavoriteMovie> findByUserAndMedia(User user, Media media);
    boolean existsByUserAndMedia(User user, Media media);
}
