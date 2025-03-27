package com.cinenexus.backend.dto.favoriteMovie;

import com.cinenexus.backend.model.media.FavoriteMovie;
import com.cinenexus.backend.model.whatchlist.WatchlistStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavoriteMovieResponseDTO {
    private Long id;
    private Long userId;
    private Long mediaId;
    private Long tmdbId;
    private String title;
    private String posterUrl;
    private String BackdropUrl;
    private String overview;
    private Double voteAverage;


    public FavoriteMovieResponseDTO toDto(FavoriteMovie favoriteMovie){
        FavoriteMovieResponseDTO dto = new FavoriteMovieResponseDTO();
    dto.setId(favoriteMovie.getId());
    dto.setUserId(favoriteMovie.getUser().getId());
    dto.setMediaId(favoriteMovie.getMedia().getId());
    dto.setTmdbId(favoriteMovie.getMedia().getTmdbId());
    dto.setTitle(favoriteMovie.getMedia().getTitle());
    dto.setPosterUrl(favoriteMovie.getMedia().getPosterUrl());
    dto.setBackdropUrl(favoriteMovie.getMedia().getBackdropUrl());
    dto.setOverview(favoriteMovie.getMedia().getOverview());
    dto.setVoteAverage(favoriteMovie.getMedia().getVoteAverage());
    return dto;
    }
}
