package com.cinenexus.backend.service;

import com.cinenexus.backend.dto.friendship.FriendRequestResponseDTO;
import com.cinenexus.backend.dto.friendship.FriendResponseDTO;
import com.cinenexus.backend.dto.friendship.FriendshipRequestDTO;
import com.cinenexus.backend.dto.friendship.FriendshipResponseDTO;
import com.cinenexus.backend.enumeration.FriendRequestStatusType;
import com.cinenexus.backend.enumeration.FriendshipStatusType;
import com.cinenexus.backend.model.user.*;
import com.cinenexus.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
  private final FriendshipRepository friendshipRepository;
  private final UserRepository userRepository;
  private final FriendRequestStatusRepository friendRequestStatusRepository;
  private final FriendshipStatusRepository friendshipStatusRepository;
  private final FriendResponseDTO friendResponseDTO;
  private final FriendRequestRepository friendRequestRepository;
  private final FriendRequestResponseDTO friendRequestResponseDTO;

  public FriendshipService(
      FriendshipRepository friendshipRepository,
      UserRepository userRepository,
      FriendRequestStatusRepository friendRequestStatusRepository,
      FriendshipStatusRepository friendshipStatusRepository,
      FriendResponseDTO friendResponseDTO,
      FriendRequestRepository friendRequestRepository,
      FriendRequestResponseDTO friendRequestResponseDTO) {
    this.friendshipRepository = friendshipRepository;
    this.userRepository = userRepository;
    this.friendRequestStatusRepository = friendRequestStatusRepository;
    this.friendshipStatusRepository = friendshipStatusRepository;
    this.friendResponseDTO = friendResponseDTO;
    this.friendRequestRepository = friendRequestRepository;
    this.friendRequestResponseDTO = friendRequestResponseDTO;
  }

  @Transactional
  public FriendshipResponseDTO sendFriendRequest(Long userId, FriendshipRequestDTO request) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    User friend =
        userRepository
            .findById(request.getFriendId())
            .orElseThrow(() -> new RuntimeException("Friend not found"));

    if (friendshipRepository.existsByUserAndFriend(user, friend)) {
      throw new RuntimeException("Friend request already sent or accepted");
    }

    FriendRequestStatus pendingStatus =
        friendRequestStatusRepository
            .findByName(FriendRequestStatusType.PENDING)
            .orElseThrow(() -> new RuntimeException("PENDING status not found"));

    FriendshipStatus notFriendStatus =
            friendshipStatusRepository.findByName(FriendshipStatusType.NOT_FRIEND)
                    .orElseThrow(()->new EntityNotFoundException("NOT_FRIEND status not found"));

    Friendship friendship = new Friendship();
    friendship.setUser(user);
    friendship.setFriend(friend);
    friendship.setRequestStatus(pendingStatus);
    friendship.setFriendshipStatus(notFriendStatus);

    friendshipRepository.save(friendship);
    return new FriendshipResponseDTO(
        friendship.getId(),
        user.getId(),
        user.getUsername(),
        user.getName(),
        user.getSurname(),
        user.getProfileImage(),
        friend.getId(),
        friend.getUsername(),
        friend.getName(),
        friend.getSurname(),
        friend.getProfileImage(),
        friendship.getRequestStatus().getName().name(),
        null);
  }

  @Transactional
  public FriendshipResponseDTO acceptFriendRequest(Long friendshipId) {
    Friendship friendship =
        friendshipRepository
            .findById(friendshipId)
            .orElseThrow(() -> new RuntimeException("Friendship not found"));

    FriendRequestStatus acceptedRequest =
        friendRequestStatusRepository
            .findByName(FriendRequestStatusType.ACCEPTED)
            .orElseThrow(() -> new RuntimeException("ACCEPTED status not found"));
    FriendshipStatus acceptedFriendship =
        friendshipStatusRepository
            .findByName(FriendshipStatusType.ACCEPTED)
            .orElseThrow(() -> new RuntimeException("ACCEPTED friendship status not found"));

    friendship.setRequestStatus(acceptedRequest);
    friendship.setFriendshipStatus(acceptedFriendship);

    Friendship savedFriendShip = friendshipRepository.save(friendship);
    Friendship otherSideFriendShip = new Friendship();
    otherSideFriendShip.setUser(savedFriendShip.getFriend());
    otherSideFriendShip.setFriend(savedFriendShip.getUser());
    otherSideFriendShip.setRequestStatus(acceptedRequest);
    otherSideFriendShip.setFriendshipStatus(acceptedFriendship);
    friendshipRepository.save(otherSideFriendShip);
    return new FriendshipResponseDTO(
            savedFriendShip.getId(),
            savedFriendShip.getUser().getId(),
            savedFriendShip.getUser().getUsername(),
            savedFriendShip.getUser().getName(),
            savedFriendShip.getUser().getSurname(),
            savedFriendShip.getUser().getProfileImage(),
            savedFriendShip.getFriend().getId(),
            savedFriendShip.getFriend().getUsername(),
            savedFriendShip.getFriend().getName(),
            savedFriendShip.getFriend().getSurname(),
            savedFriendShip.getFriend().getProfileImage(),
            savedFriendShip.getRequestStatus().getName().name(),
            savedFriendShip.getFriendshipStatus().getName().name());
  }

  @Transactional
  public void rejectFriendRequest(Long friendshipId) {
    Friendship friendship =
        friendshipRepository
            .findById(friendshipId)
            .orElseThrow(() -> new RuntimeException("Friendship not found"));

    FriendRequestStatus rejectedStatus =
        friendRequestStatusRepository
            .findByName(FriendRequestStatusType.REJECTED)
            .orElseThrow(() -> new RuntimeException("REJECTED status not found"));

    friendship.setRequestStatus(rejectedStatus);
    friendship.setFriendshipStatus(null);

    friendshipRepository.save(friendship);
  }

  @Transactional
  public void removeFriend(Long userId, Long friendId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    User friend =
        userRepository
            .findById(friendId)
            .orElseThrow(() -> new RuntimeException("Friend not found"));

    Friendship friendship =
        friendshipRepository
            .findByUserAndFriend(user, friend)
            .orElseThrow(() -> new RuntimeException("Friendship not found"));

    Friendship otherSideFriendship =
            friendshipRepository
                    .findByUserAndFriend(friend,user)
                            .orElseThrow(() -> new RuntimeException("OtherSide Friend not found"));

    friendshipRepository.delete(friendship);
    friendshipRepository.delete(otherSideFriendship);
  }

  public Page<FriendshipResponseDTO> getUserFriends(Long userId, int page, int size) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

    Pageable pageable = PageRequest.of(page, size);
    return friendshipRepository
        .findAllByUserOrFriend(user, user, pageable)
        .map(
            friendship ->
                new FriendshipResponseDTO(
                    friendship.getId(),
                    friendship.getUser().getId(),
                        friendship.getUser().getUsername(),
                        friendship.getUser().getName(),
                        friendship.getUser().getSurname(),
                        friendship.getUser().getProfileImage(),
                    friendship.getFriend().getId(),
                    friendship.getFriend().getUsername(),
                    friendship.getFriend().getName(),
                    friendship.getFriend().getSurname(),
                    friendship.getFriend().getProfileImage(),
                    friendship.getRequestStatus().getName().name(),
                    friendship.getFriendshipStatus() != null
                        ? friendship.getFriendshipStatus().getName().name()
                        : null));
  }

  public boolean areFriends(Long userId1, Long userId2) {
    return friendshipRepository.existsByUserIdAndFriendId(userId1, userId2)
        || friendshipRepository.existsByUserIdAndFriendId(userId2, userId1);
  }

  public List<FriendResponseDTO> getAllFriendsByUserId(Long userId){
    User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User Not Found"));
    List<Friendship> friendshipList = friendshipRepository.findAllByUser(user);
    return friendshipList.stream().map(friendResponseDTO::toDTO).toList();
  }

  public List<FriendRequestResponseDTO>  getAllFriendRequestByuserId(Long userId){
    User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User Not Found!"));
    List<FriendRequest> friendRequestList = friendRequestRepository.findAllBySender(user);
    return friendRequestList.stream().map(friendRequestResponseDTO::toDTO).toList();
  }
}
