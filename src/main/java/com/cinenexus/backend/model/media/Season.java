package com.cinenexus.backend.model.media;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "seasons")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = false)
    @JsonBackReference
    private Media media; // این فصل متعلق به کدام سریال است؟
    private String name;
    private Integer seasonNumber;
    private Integer totalEpisodes;
    private String airDate;
    private String posterPath;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Episode> episodes;

    public Season(Media media, Integer seasonNumber, Integer totalEpisodes, List<Episode> episodes) {
        this.media = media;
        this.seasonNumber = seasonNumber;
        this.totalEpisodes = totalEpisodes;
        this.episodes = episodes;
    }

    public Season(Media media, Integer seasonNumber, Integer totalEpisodes, String airDate, String posterPath) {
        this.media = media;
        this.seasonNumber = seasonNumber;
        this.totalEpisodes = totalEpisodes;
        this.airDate = airDate;
        this.posterPath = posterPath;
    }

    @Override
    public String toString() {
        return "Season{" +
                "id=" + id +
                ", seasonNumber=" + seasonNumber +
                ", totalEpisodes=" + totalEpisodes +
                '}';
    }
}
