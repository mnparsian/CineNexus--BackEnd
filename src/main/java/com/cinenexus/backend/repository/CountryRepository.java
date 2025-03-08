package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.misc.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country,Long> {
    public Optional<Country> findById(Long id);
}
