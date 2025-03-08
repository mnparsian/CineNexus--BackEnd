package com.cinenexus.backend.dto.user;

import com.cinenexus.backend.enumeration.RoleType;
import com.cinenexus.backend.enumeration.UserStatusType;
import com.cinenexus.backend.model.commentReview.Comment;

import com.cinenexus.backend.model.commentReview.Review;
import com.cinenexus.backend.model.misc.Country;
import com.cinenexus.backend.model.misc.Language;
import com.cinenexus.backend.model.payment.Payment;
import com.cinenexus.backend.model.payment.Subscription;
import com.cinenexus.backend.model.user.Role;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.model.user.UserStatus;

import com.cinenexus.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private UserStatusRepository userStatusRepository;
  @Autowired private CountryRepository countryRepository;
  @Autowired private LanguageRepository languageRepository;

  public User toEntity(UserRequestDTO dto) {
    User user = new User();
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    user.setPassword(dto.getPassword());
    RoleType roleType;
    try {
      roleType = RoleType.valueOf(dto.getRole().toUpperCase()); // تبدیل String به Enum
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid role: " + dto.getRole());
    }

    Role role =
        roleRepository
            .findByName(roleType)
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "The Role with this name " + dto.getRole() + " not found"));
    user.setRole(role);

    user.setName(dto.getName());
    user.setSurname(dto.getSurname());
    user.setBio(dto.getBio());
    user.setProfileImage(dto.getProfileImage());

    UserStatus userStatus =
        userStatusRepository
            .findByName(UserStatusType.ACTIVE)
            .orElseThrow(() -> new RuntimeException("Status Not Found!"));
    user.setStatus(userStatus);
    user.setBirthday(dto.getBirthday());

    Country country =
        countryRepository
            .findById(dto.getCountry_id())
            .orElseThrow(() -> new RuntimeException("Country Not Found!"));
    user.setCountry(country);

    Language language =
        languageRepository
            .findById(dto.getPreferredLanguage_id())
            .orElseThrow(() -> new RuntimeException("Language not found!"));
    user.setPreferredLanguage(language);

    user.setPhoneNumber(dto.getPhoneNumber());

    return user;
  }

  public UserResponseDTO toDTO(User user) {
    UserResponseDTO dto = new UserResponseDTO();
    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    dto.setEmail(user.getEmail());
    dto.setRole(user.getRole().getName().name());
    dto.setName(user.getName());
    dto.setSurname(user.getSurname());
    dto.setBio(user.getBio());
    dto.setProfileImage(user.getProfileImage());
    dto.setIsVerified(user.getIsVerified());
    dto.setStatus(user.getStatus().getName().name());
    dto.setLastLogin(user.getLastLogin());
    dto.setCreatedAt(user.getCreatedAt());
    dto.setBirthday(user.getBirthday());
    dto.setPreferredLanguage(user.getPreferredLanguage().getName());
    dto.setCountry(user.getCountry().getName());
    dto.setPhoneNumber(user.getPhoneNumber());
    dto.setSocialLinks(user.getSocialLinks());

    List<Long> friendsId =
        user.getFriends().stream().map(friendship -> friendship.getFriend().getId()).toList();
    dto.setFriendIds(friendsId);

    List<Long> favoriteMovieIds =
        user.getFavoriteMovies().stream()
            .map(favoriteMovie -> favoriteMovie.getMedia().getId())
            .toList();
    dto.setFavoriteMovieIds(favoriteMovieIds);

    List<Long> watchlistIds =
        user.getWatchlist().stream()
            .map(userWatchlist -> userWatchlist.getMedia().getId())
            .toList();
    dto.setWatchlistIds(watchlistIds);

    List<Long> reviewIds = user.getReviews().stream().map(Review::getId).toList();
    dto.setReviewIds(reviewIds);

    List<Long> commentIds = user.getComments().stream().map(Comment::getId).toList();
    dto.setCommentIds(commentIds);

    List<Long> subscriptionIds = user.getSubscriptions().stream().map(Subscription::getId).toList();
    dto.setSubscriptionIds(subscriptionIds);

    List<Long> paymentIds = user.getPayments().stream().map(Payment::getId).toList();
    dto.setPaymentIds(paymentIds);

    return dto;
  }

  public User updateUser(User user, UserRequestDTO dto) {
    if (dto.getUsername() != null) {
      user.setUsername(dto.getUsername());
    }
    if (dto.getEmail() != null) {
      user.setEmail(dto.getEmail());
    }

    if (dto.getPassword() != null) {
      user.setPassword(dto.getPassword());
    }

    if (dto.getRole() != null) {
      RoleType roleType;
      try {
        roleType = RoleType.valueOf(dto.getRole().toUpperCase()); // تبدیل String به Enum
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid role: " + dto.getRole());
      }
      Role role =
          roleRepository
              .findByName(roleType)
              .orElseThrow(
                  () ->
                      new RuntimeException(
                          "The Role with this name " + dto.getRole() + " not found"));
      user.setRole(role);
    }
    if (dto.getName() != null) {
      user.setName(dto.getName());
    }
    if (dto.getSurname() != null) {
      user.setSurname(dto.getSurname());
    }
    if (dto.getBio() != null) {
      user.setBio(dto.getBio());
    }
    if (dto.getProfileImage() != null) {
      user.setProfileImage(dto.getProfileImage());
    }
    if (dto.getCountry_id() != null) {
      Country country =
          countryRepository
              .findById(dto.getCountry_id())
              .orElseThrow(() -> new RuntimeException("Country Not Found!"));
      user.setCountry(country);
    }
    if (dto.getPreferredLanguage_id() != null) {
      Language language =
          languageRepository
              .findById(dto.getPreferredLanguage_id())
              .orElseThrow(() -> new RuntimeException("Language not found!"));
      user.setPreferredLanguage(language);
    }
    if (dto.getPhoneNumber() != null) {
      user.setPhoneNumber(dto.getPhoneNumber());
    }
    return user;
  }
}
