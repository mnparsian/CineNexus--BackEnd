package com.cinenexus.backend.controller;



import com.cinenexus.backend.dto.friendship.FriendRequestResponseDTO;
import com.cinenexus.backend.dto.friendship.FriendResponseDTO;
import com.cinenexus.backend.dto.friendship.FriendshipRequestDTO;
import com.cinenexus.backend.dto.friendship.FriendshipResponseDTO;
import com.cinenexus.backend.service.FriendshipService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{userId}")
    public ResponseEntity<FriendshipResponseDTO> sendFriendRequest(@PathVariable Long userId, @RequestBody FriendshipRequestDTO request) {
        return ResponseEntity.ok(friendshipService.sendFriendRequest(userId, request));
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{friendshipId}/accept")
    public ResponseEntity<FriendshipResponseDTO> acceptFriendRequest(@PathVariable Long friendshipId) {
        return ResponseEntity.ok(friendshipService.acceptFriendRequest(friendshipId));
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{friendshipId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long friendshipId) {
        friendshipService.rejectFriendRequest(friendshipId);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        friendshipService.removeFriend(userId, friendId);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<Page<FriendshipResponseDTO>> getUserFriends(@PathVariable Long userId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(friendshipService.getUserFriends(userId, page, size));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<FriendResponseDTO>> getAllFriendsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(friendshipService.getAllFriendsByUserId(userId));
    }

    @GetMapping("/requests/{userId}")
    public ResponseEntity<List<FriendRequestResponseDTO>> getAllFriendRequestByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(friendshipService.getAllFriendRequestByuserId(userId));
    }
}