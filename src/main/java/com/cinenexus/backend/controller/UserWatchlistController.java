package com.cinenexus.backend.controller;


import com.cinenexus.backend.model.whatchlist.UserWatchlist;
import com.cinenexus.backend.service.UserWatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class UserWatchlistController {

    private final UserWatchlistService userWatchlistService;

    public UserWatchlistController(UserWatchlistService userWatchlistService) {
        this.userWatchlistService = userWatchlistService;
    }

    @PostMapping("/add")
    public ResponseEntity<UserWatchlist> addToWatchlist(@RequestParam Long userId, @RequestParam Long mediaId, @RequestParam Long statusId) {
        return ResponseEntity.ok(userWatchlistService.addToWatchlist(userId, mediaId, statusId));
    }
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromWatchlist(@RequestParam Long userId, @RequestParam Long mediaId) {
        userWatchlistService.removeFromWatchlist(userId, mediaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserWatchlist>> getWatchlistByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userWatchlistService.getWatchlistByUserId(userId));
    }
    @PutMapping("/update-status")
    public ResponseEntity<UserWatchlist> updateWatchlistStatus(@RequestParam Long userId, @RequestParam Long mediaId, @RequestParam Long statusId) {
        return ResponseEntity.ok(userWatchlistService.updateWatchlistStatus(userId, mediaId, statusId));
    }

}

