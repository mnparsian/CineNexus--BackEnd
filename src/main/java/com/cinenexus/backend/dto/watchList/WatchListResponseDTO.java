package com.cinenexus.backend.dto.watchList;

import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.model.whatchlist.UserWatchlist;
import com.cinenexus.backend.model.whatchlist.WatchlistStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WatchListResponseDTO {
  private Long id;
  private Long userId;
  private Long mediaId;
  private String title;
  private String posterUrl;
  private String BackdropUrl;
  private String overview;
  private Double voteAverage;
  private WatchlistStatus status;

  public WatchListResponseDTO toDto(UserWatchlist watchlist) {
    WatchListResponseDTO dto = new WatchListResponseDTO();
    dto.setId(watchlist.getId());
    dto.setUserId(watchlist.getUser().getId());
    dto.setMediaId(watchlist.getMedia().getId());
    dto.setTitle(watchlist.getMedia().getTitle());
    dto.setPosterUrl(watchlist.getMedia().getPosterUrl());
    dto.setBackdropUrl(watchlist.getMedia().getBackdropUrl());
    dto.setOverview(watchlist.getMedia().getOverview());
    dto.setVoteAverage(watchlist.getMedia().getVoteAverage());
    dto.setStatus(watchlist.getStatus());
    return dto;
  }
}
