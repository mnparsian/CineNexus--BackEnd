package com.cinenexus.backend.service;


import com.cinenexus.backend.dto.friendship.FriendshipRequestDTO;
import com.cinenexus.backend.dto.friendship.FriendshipResponseDTO;
import com.cinenexus.backend.enumeration.FriendRequestStatusType;
import com.cinenexus.backend.enumeration.FriendshipStatusType;
import com.cinenexus.backend.model.user.*;
import com.cinenexus.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final FriendRequestStatusRepository friendRequestStatusRepository;
    private final FriendshipStatusRepository friendshipStatusRepository;

    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository,
                             FriendRequestStatusRepository friendRequestStatusRepository,
                             FriendshipStatusRepository friendshipStatusRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.friendRequestStatusRepository = friendRequestStatusRepository;
        this.friendshipStatusRepository = friendshipStatusRepository;
    }

    @Transactional
    public FriendshipResponseDTO sendFriendRequest(Long userId, FriendshipRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(request.getFriendId())
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        if (friendshipRepository.existsByUserAndFriend(user, friend)) {
            throw new RuntimeException("Friend request already sent or accepted");
        }

        FriendRequestStatus pendingStatus = friendRequestStatusRepository.findByName(FriendRequestStatusType.PENDING)
                .orElseThrow(() -> new RuntimeException("PENDING status not found"));

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setRequestStatus(pendingStatus);
        friendship.setFriendshipStatus(null);

        friendshipRepository.save(friendship);
        return new FriendshipResponseDTO(friendship.getId(), user.getId(), friend.getId(), friendship.getRequestStatus().getName().name(), null);
    }

    @Transactional
    public FriendshipResponseDTO acceptFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));

        FriendRequestStatus acceptedRequest = friendRequestStatusRepository.findByName(FriendRequestStatusType.ACCEPTED)
                .orElseThrow(() -> new RuntimeException("ACCEPTED status not found"));
        FriendshipStatus acceptedFriendship = friendshipStatusRepository.findByName(FriendshipStatusType.ACCEPTED)
                .orElseThrow(() -> new RuntimeException("ACCEPTED friendship status not found"));

        friendship.setRequestStatus(acceptedRequest);
        friendship.setFriendshipStatus(acceptedFriendship);

        friendshipRepository.save(friendship);
        return new FriendshipResponseDTO(friendship.getId(), friendship.getUser().getId(), friendship.getFriend().getId(), friendship.getRequestStatus().getName().name(), friendship.getFriendshipStatus().getName().name());
    }

    @Transactional
    public void rejectFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));

        FriendRequestStatus rejectedStatus = friendRequestStatusRepository.findByName(FriendRequestStatusType.REJECTED)
                .orElseThrow(() -> new RuntimeException("REJECTED status not found"));

        friendship.setRequestStatus(rejectedStatus);
        friendship.setFriendshipStatus(null);

        friendshipRepository.save(friendship);
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        Friendship friendship = friendshipRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));

        friendshipRepository.delete(friendship);
    }

    public Page<FriendshipResponseDTO> getUserFriends(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        return friendshipRepository.findAllByUserOrFriend(user, user, pageable)
                .map(friendship -> new FriendshipResponseDTO(
                        friendship.getId(),
                        friendship.getUser().getId(),
                        friendship.getFriend().getId(),
                        friendship.getRequestStatus().getName().name(),
                        friendship.getFriendshipStatus() != null ? friendship.getFriendshipStatus().getName().name() : null
                ));
    }
}