package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.whatchlist.UserWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserWatchlistRepository extends JpaRepository<UserWatchlist,Long> {
    List<UserWatchlist> findByUserIdAndMediaId(Long userId, Long mediaId);

    public List<UserWatchlist> findByUserId(Long userId);
}
