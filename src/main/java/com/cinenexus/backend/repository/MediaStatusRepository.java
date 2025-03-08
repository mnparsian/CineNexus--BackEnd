package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.MediaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaStatusRepository extends JpaRepository<MediaStatus,Long> {
    Optional<MediaStatus> findByName(String name);
}
