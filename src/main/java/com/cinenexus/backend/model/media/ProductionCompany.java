package com.cinenexus.backend.model.media;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "production_companies")
public class ProductionCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tmdbId;

    @Column(nullable = false)
    private String name;

    private String logoPath;
    private String originCountry;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = false)
    @JsonBackReference
    private Media media;

    public ProductionCompany(Long tmdbId, String name, String logoPath, String originCountry, Media media) {
        this.tmdbId = tmdbId;
        this.name = name;
        this.logoPath = logoPath;
        this.originCountry = originCountry;
        this.media = media;
    }
}
