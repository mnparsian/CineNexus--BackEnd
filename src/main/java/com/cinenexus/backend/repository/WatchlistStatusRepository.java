package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.whatchlist.WatchlistStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistStatusRepository extends JpaRepository<WatchlistStatus,Long> {}
