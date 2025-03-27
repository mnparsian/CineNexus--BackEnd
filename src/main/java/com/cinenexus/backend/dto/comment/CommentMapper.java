package com.cinenexus.backend.dto.comment;

import com.cinenexus.backend.model.commentReview.Comment;
import com.cinenexus.backend.model.commentReview.Review;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.CommentRepository;
import com.cinenexus.backend.repository.MediaRepository;
import com.cinenexus.backend.repository.ReviewRepository;
import com.cinenexus.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class CommentMapper {

  private final UserRepository userRepository;
  private final ReviewRepository reviewRepository;
  private final MediaRepository mediaRepository;
  private final CommentRepository commentRepository;

  public CommentMapper(
      UserRepository userRepository,
      ReviewRepository reviewRepository,
      MediaRepository mediaRepository,
      CommentRepository commentRepository) {
    this.userRepository = userRepository;
    this.reviewRepository = reviewRepository;
    this.mediaRepository = mediaRepository;
    this.commentRepository = commentRepository;
  }

  public Comment toEntity(CommentRequestDTO dto) {
    Comment comment = new Comment();

    User user =
        userRepository
            .findById(dto.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    comment.setUser(user);
    if (dto.getReviewId() != null) {
      Review review =
          reviewRepository
              .findById(dto.getReviewId())
              .orElseThrow(() -> new EntityNotFoundException("Review not found"));
      comment.setReview(review);
        Media media =
                mediaRepository
                        .findById(review.getMedia().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        comment.setMedia(media);

    }

    if (dto.getMediaId() != null) {
      Media media =
          mediaRepository
              .findById(dto.getMediaId())
              .orElseThrow(() -> new EntityNotFoundException("Review not found"));
      comment.setMedia(media);
    }
    if (dto.getParentCommentId() != null) {
      Comment parentComment =
          commentRepository
              .findById(dto.getParentCommentId())
              .orElseThrow(() -> new EntityNotFoundException("Parent Comment not found"));
      comment.setParentComment(parentComment);
    }

    comment.setCreatedAt(LocalDateTime.now());

    comment.setContent(dto.getContent());
    return comment;
  }

  public CommentResponseDTO toDTO(Comment comment) {
    CommentResponseDTO dto = new CommentResponseDTO();
    dto.setId(comment.getId());
    dto.setUserId(comment.getUser().getId());
    dto.setUsername(comment.getUser().getUsername());
    dto.setProfileImage(comment.getUser().getProfileImage());
    if(comment.getReview() != null){
        dto.setReviewId(comment.getReview().getId());
    }
    if(comment.getMedia() != null){
        dto.setMediaId(comment.getMedia().getId());
    }

    if(comment.getParentComment() != null){
        dto.setParentCommentId(comment.getParentComment().getId());
    }

    if(comment.getReplies() != null){
        List<Long> repliesIds = comment.getReplies().stream().map(comment1 -> comment.getId()).toList();
        dto.setRepliesIds(repliesIds);
    }

    dto.setContent(comment.getContent());
    dto.setCreatedAt(comment.getCreatedAt());
    dto.setUpdatedAt(comment.getUpdatedAt());
    return dto;
  }
}
