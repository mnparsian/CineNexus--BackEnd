package com.cinenexus.backend.model.whatchlist;

import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_watchlist")
public class UserWatchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = false)
    @JsonBackReference
    private Media media;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private WatchlistStatus status;
}
