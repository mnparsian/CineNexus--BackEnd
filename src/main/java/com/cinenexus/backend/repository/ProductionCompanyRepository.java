package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.ProductionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionCompanyRepository extends JpaRepository<ProductionCompany,Long> {
    public Optional<ProductionCompany> findByTmdbId(Long tmdbId);
    @Query("SELECT pc FROM ProductionCompany pc JOIN FETCH pc.media m WHERE m.id = :mediaId")
    List<ProductionCompany> findAllByMediaId(@Param("mediaId") Long mediaId);
}
