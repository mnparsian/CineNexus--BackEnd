package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.CrewRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewRoleRepository extends JpaRepository<CrewRole, Long> {
    public Optional<CrewRole> findByName(String name);
}

