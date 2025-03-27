package com.cinenexus.backend.dto.review;

import com.cinenexus.backend.model.commentReview.Review;
import com.cinenexus.backend.model.commentReview.ReviewLike;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.MediaRepository;
import com.cinenexus.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class ReviewMapper {

  private  UserRepository userRepository;
  private  MediaRepository mediaRepository;

  public ReviewMapper(UserRepository userRepository, MediaRepository mediaRepository) {
    this.userRepository = userRepository;
    this.mediaRepository = mediaRepository;
  }

  public ReviewMapper() {
  }

  public Review toEntity(ReviewRequestDTO dto) {
    Review review = new Review();

    User user =
        userRepository
            .findById(dto.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    review.setUser(user);

    Media media =
        mediaRepository
            .findById(dto.getMediaId())
            .orElseThrow(() -> new EntityNotFoundException("Media not found"));
    review.setMedia(media);

    review.setContent(dto.getContent());
    review.setRating(dto.getRating());
    review.setCreatedAt(LocalDateTime.now());

    return review;
  }

  public ReviewResponseDTO toDTO(Review review) {
    ReviewResponseDTO dto = new ReviewResponseDTO();
    dto.setId(review.getId());
    dto.setUserId(review.getUser().getId());
    dto.setMediaId(review.getMedia().getId());
    dto.setMediaTitle(review.getMedia().getTitle());
    dto.setMediaPosterUrl(review.getMedia().getPosterUrl());
    dto.setMediaOverview(review.getMedia().getOverview());
    dto.setContent(review.getContent());
    dto.setRating(review.getRating());
    dto.setCreatedAt(review.getCreatedAt());
    List<Long> likesIds = review.getLikes().stream()
            .map(like -> like.getUser().getId())
            .collect(Collectors.toList());
    dto.setLikeIds(likesIds);

    dto.setUpdatedAt(review.getUpdatedAt());
    return dto;
  }
}
