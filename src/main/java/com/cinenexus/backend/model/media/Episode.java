package com.cinenexus.backend.model.media;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    @JsonBackReference
    private Season season;

    private Integer episodeNumber;
    @Column(length = 1000)
    private String title;
    @Column(length = 1000)
    private String releaseDate;
    @Column(columnDefinition = "TEXT",length = 1000)
    private String overview;
    private Long tmdbId;
    private Integer duration;
    @Column(length = 1000)
    private String videoUrl;
}
