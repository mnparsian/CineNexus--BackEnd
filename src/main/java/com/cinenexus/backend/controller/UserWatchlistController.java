package com.cinenexus.backend.controller;


import com.cinenexus.backend.dto.watchList.WatchListResponseDTO;
import com.cinenexus.backend.model.whatchlist.UserWatchlist;
import com.cinenexus.backend.service.UserWatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class UserWatchlistController {

    private final UserWatchlistService userWatchlistService;

    public UserWatchlistController(UserWatchlistService userWatchlistService) {
        this.userWatchlistService = userWatchlistService;
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<UserWatchlist> addToWatchlist(@RequestParam Long userId, @RequestParam Long mediaId, @RequestParam Long statusId) {
        return ResponseEntity.ok(userWatchlistService.addToWatchlist(userId, mediaId, statusId));
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromWatchlist(@RequestParam Long userId, @RequestParam Long mediaId) {
        userWatchlistService.removeFromWatchlist(userId, mediaId);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchListResponseDTO>> getWatchlistByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userWatchlistService.getWatchlistByUserId(userId, userDetails));
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/update-status")
    public ResponseEntity<UserWatchlist> updateWatchlistStatus(@RequestParam Long userId, @RequestParam Long mediaId, @RequestParam Long statusId) {
        return ResponseEntity.ok(userWatchlistService.updateWatchlistStatus(userId, mediaId, statusId));
    }

}

