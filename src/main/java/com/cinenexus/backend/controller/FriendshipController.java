package com.cinenexus.backend.controller;



import com.cinenexus.backend.dto.friendship.FriendshipRequestDTO;
import com.cinenexus.backend.dto.friendship.FriendshipResponseDTO;
import com.cinenexus.backend.service.FriendshipService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    // ارسال درخواست دوستی
    @PostMapping("/{userId}")
    public ResponseEntity<FriendshipResponseDTO> sendFriendRequest(@PathVariable Long userId, @RequestBody FriendshipRequestDTO request) {
        return ResponseEntity.ok(friendshipService.sendFriendRequest(userId, request));
    }

    // پذیرش درخواست دوستی
    @PutMapping("/{friendshipId}/accept")
    public ResponseEntity<FriendshipResponseDTO> acceptFriendRequest(@PathVariable Long friendshipId) {
        return ResponseEntity.ok(friendshipService.acceptFriendRequest(friendshipId));
    }

    // رد کردن درخواست دوستی
    @PutMapping("/{friendshipId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long friendshipId) {
        friendshipService.rejectFriendRequest(friendshipId);
        return ResponseEntity.noContent().build();
    }

    // حذف دوست
    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        friendshipService.removeFriend(userId, friendId);
        return ResponseEntity.noContent().build();
    }

    // دریافت لیست دوستان یک کاربر با pagination
    @GetMapping("/{userId}")
    public ResponseEntity<Page<FriendshipResponseDTO>> getUserFriends(@PathVariable Long userId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(friendshipService.getUserFriends(userId, page, size));
    }
}