package com.cinenexus.backend.dto.media;

import com.cinenexus.backend.model.media.Media;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponseDTO {
    private Long id;
    private String title;
    private String originalTitle;
    private String description;
    private String category;
    private String posterUrl;
    private String backdropUrl;
    private Double voteAverage;
    private Integer voteCount;
    private LocalDate releaseDate;
    private boolean isTVShow;


    public MediaResponseDTO fromEntity(Media media) {
        return new MediaResponseDTO(
                media.getId(),
                media.getTitle(),
                media.getOriginalTitle(),
                media.getDescription(),
                media.getCategory(),
                media.getPosterUrl(),
                media.getBackdropUrl(),
                media.getVoteAverage(),
                media.getVoteCount(),
                media.getReleaseDate(),
                media.isTVShow()
        );
    }
}

