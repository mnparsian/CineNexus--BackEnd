package com.cinenexus.backend.model.media;

import com.cinenexus.backend.model.misc.Country;
import com.cinenexus.backend.model.misc.Language;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long tmdbId;

    @Column(nullable = false, length = 1000)
    private String title;
    @Column(nullable = false, length = 1000)
    private String originalTitle;
    @Column(nullable = true, length = 1000)
    private String description;
    private LocalDate releaseDate;
    private Integer runtime;
    @Column(length = 1000)
    private String posterUrl;
    @Column(length = 1000)
    private String backdropUrl;
    @Column(length = 500)
    private String tagline;
    private String homepage;
    private Long budget;
    private Long revenue;
    @Column(length = 1000)
    private String overview;
    private Integer voteCount;
    private Double voteAverage;
    private Double popularity;
    @ManyToOne
    @JoinColumn(name = "media_type_id", nullable = false)
    private MediaType mediaType;
    @Column(name = "category", nullable = true)
    private String category;




    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private MediaStatus status;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MediaCrew> mediaCrew;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Season> seasons;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductionCompany> productionCompanies;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<MediaGenre> mediaGenres;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isTVShow = false;


    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", tmdbId=" + tmdbId +
                ", title='" + title + '\'' +
                '}';
    }

    public Media(Long id) {
        this.id = id;
    }
}
