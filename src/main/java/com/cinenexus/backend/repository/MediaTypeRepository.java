package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaTypeRepository extends JpaRepository<MediaType,Long> {
    public Optional<MediaType> findByName(String name);
}
