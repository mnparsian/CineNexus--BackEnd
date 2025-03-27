package com.cinenexus.backend.service;

import com.cinenexus.backend.dto.watchList.WatchListResponseDTO;
import com.cinenexus.backend.enumeration.RoleType;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.whatchlist.UserWatchlist;
import com.cinenexus.backend.model.whatchlist.WatchlistStatus;
import com.cinenexus.backend.repository.UserRepository;
import com.cinenexus.backend.repository.MediaRepository;
import com.cinenexus.backend.repository.UserWatchlistRepository;
import com.cinenexus.backend.repository.WatchlistStatusRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserWatchlistService {

    private final UserWatchlistRepository userWatchlistRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final WatchlistStatusRepository watchlistStatusRepository;
    private final FriendshipService friendshipService;
    private final WatchListResponseDTO watchListResponseDTO;

    public UserWatchlistService(UserWatchlistRepository userWatchlistRepository, UserRepository userRepository, MediaRepository mediaRepository , WatchlistStatusRepository watchlistStatusRepository,FriendshipService friendshipService,WatchListResponseDTO watchListResponseDTO) {
        this.userWatchlistRepository = userWatchlistRepository;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        this.watchlistStatusRepository = watchlistStatusRepository;
        this.friendshipService = friendshipService;
        this.watchListResponseDTO = watchListResponseDTO;
    }

    public UserWatchlist addToWatchlist(Long userId, Long mediaId, Long statusId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));
        WatchlistStatus status = watchlistStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("WatchlistStatus not found"));

        List<UserWatchlist> existingEntries = userWatchlistRepository.findByUserIdAndMediaId(userId, mediaId);

        if (!existingEntries.isEmpty()) {
            UserWatchlist existingWatchlist = existingEntries.get(0);
            existingWatchlist.setStatus(status);
            return userWatchlistRepository.save(existingWatchlist);
        }

        UserWatchlist userWatchlist = new UserWatchlist();
        userWatchlist.setUser(user);
        userWatchlist.setMedia(media);
        userWatchlist.setStatus(status);

        return userWatchlistRepository.save(userWatchlist);
    }

    public void removeFromWatchlist(Long userId, Long mediaId) {
        List<UserWatchlist> existingEntries = userWatchlistRepository.findByUserIdAndMediaId(userId, mediaId);

        if (existingEntries.isEmpty()) {
            throw new RuntimeException("Watchlist entry not found");
        }

        userWatchlistRepository.deleteAll(existingEntries);
    }

    public UserWatchlist updateWatchlistStatus(Long userId, Long mediaId, Long statusId) {
        List<UserWatchlist> existingEntries = userWatchlistRepository.findByUserIdAndMediaId(userId, mediaId);

        if (existingEntries.isEmpty()) {
            throw new RuntimeException("Watchlist entry not found");
        }

        UserWatchlist userWatchlist = existingEntries.get(0);
        WatchlistStatus status = watchlistStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("WatchlistStatus not found"));

        userWatchlist.setStatus(status);
        return userWatchlistRepository.save(userWatchlist);
    }
    public List<WatchListResponseDTO> getWatchlistByUserId(Long userId, UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // اگه ادمین بود، اجازه بده
//        if (currentUser.getRole().name().equals("ADMIN")) {
//            List<UserWatchlist> watchlist = userWatchlistRepository.findByUserId(userId);
//            return watchlist.stream().map(watchListResponseDTO::toDto).toList();
//        }
//
//        // اگه خودش بود یا دوستش بود، اجازه بده
//        if (currentUser.getId().equals(userId) || friendshipService.areFriends(currentUser.getId(), userId)) {
//            List<UserWatchlist> watchlist = userWatchlistRepository.findByUserId(userId);
//            return watchlist.stream().map(watchListResponseDTO::toDto).toList();
//        }
//
//        // در غیر این صورت، خطای دسترسی بده
//        throw new AccessDeniedException("You are not allowed to view this watchlist.");

        List<UserWatchlist> watchlist = userWatchlistRepository.findByUserId(userId);
           return watchlist.stream().map(watchListResponseDTO::toDto).toList();
    }

}
