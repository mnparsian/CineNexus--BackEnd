package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.misc.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Long> {
    public Optional<Language> findById(Long id);
    Optional<Language> findByName(String name);
    Optional<Language> findByCode(String code);
}
