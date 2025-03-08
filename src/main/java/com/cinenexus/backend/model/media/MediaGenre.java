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
@Table(name = "media_genres")
public class MediaGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = false)
    @JsonBackReference
    private Media media;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public MediaGenre(Media media, Genre genre) {
        this.media = media;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "MediaGenre{" +
                "id=" + id +
                ", genre=" + genre +
                '}';
    }
}
