package com.cinenexus.backend.service;

import com.cinenexus.backend.model.commentReview.*;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewCommentService {

  private final ReviewRepository reviewRepository;
  private final CommentRepository commentRepository;
  private final ReviewLikeRepository reviewLikeRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final UserRepository userRepository;
  private final MediaRepository mediaRepository;

  public ReviewCommentService(
      ReviewRepository reviewRepository,
      CommentRepository commentRepository,
      ReviewLikeRepository reviewLikeRepository,
      CommentLikeRepository commentLikeRepository,
      UserRepository userRepository,
      MediaRepository mediaRepository) {
    this.reviewRepository = reviewRepository;
    this.commentRepository = commentRepository;
    this.reviewLikeRepository = reviewLikeRepository;
    this.commentLikeRepository = commentLikeRepository;
    this.userRepository = userRepository;
    this.mediaRepository = mediaRepository;
  }

  // Review Methods
  public Review createReview(Review review) {
    // دریافت کامل کاربر از دیتابیس
    User user =
        userRepository
            .findById(review.getUser().getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    // دریافت کامل فیلم/سریال از دیتابیس
    Media media =
        mediaRepository
            .findById(review.getMedia().getId())
            .orElseThrow(() -> new RuntimeException("Media not found"));

    // تنظیم اطلاعات دریافت‌شده
    review.setUser(user);
    review.setMedia(media);
    review.setCreatedAt(LocalDateTime.now());

    return reviewRepository.save(review);
  }

  public Optional<Review> getReviewById(Long id) {
    return reviewRepository.findById(id);
  }

  public List<Review> getReviewsByMedia(Long mediaId) {
    return reviewRepository.findByMediaId(mediaId);
  }

  public Review updateReview(Long id, String content, Double rating) {
    return reviewRepository
        .findById(id)
        .map(
            review -> {
              review.setContent(content);
              review.setRating(rating);
              review.setUpdatedAt(LocalDateTime.now());
              return reviewRepository.save(review);
            })
        .orElseThrow(() -> new RuntimeException("Review not found"));
  }

  public void deleteReview(Long id) {
    reviewRepository.deleteById(id);
  }

  // Comment Methods
  public Comment createComment(Comment comment) {
    // دریافت کامل کاربر از دیتابیس
    User user = userRepository.findById(comment.getUser().getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    // دریافت کامل نقد از دیتابیس
    Review review = reviewRepository.findById(comment.getReview().getId())
            .orElseThrow(() -> new RuntimeException("Review not found"));

    // تنظیم اطلاعات دریافت‌شده
    comment.setUser(user);
    comment.setReview(review);
    comment.setCreatedAt(LocalDateTime.now());

    return commentRepository.save(comment);
  }


  public List<Comment> getCommentsByReview(Long reviewId) {
    return commentRepository.findByReviewId(reviewId);
  }

  public Comment updateComment(Long id, String content) {
    return commentRepository
        .findById(id)
        .map(
            comment -> {
              comment.setContent(content);
              comment.setUpdatedAt(LocalDateTime.now());
              return commentRepository.save(comment);
            })
        .orElseThrow(() -> new RuntimeException("Comment not found"));
  }

  public void deleteComment(Long id) {
    commentRepository.deleteById(id);
  }

  // Review Like Methods
  public ReviewLike likeReview(Long reviewId, Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("Review not found"));

    ReviewLike reviewLike = new ReviewLike();
    reviewLike.setUser(user);
    reviewLike.setReview(review);
    return reviewLikeRepository.save(reviewLike);
  }

  public void unlikeReview(Long id) {
    reviewLikeRepository.deleteById(id);
  }

  // Comment Like Methods
  public CommentLike likeComment(Long commentId, Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("Comment not found"));

    CommentLike commentLike = new CommentLike();
    commentLike.setUser(user);
    commentLike.setComment(comment);
    return commentLikeRepository.save(commentLike);
  }

  public void unlikeComment(Long id) {
    commentLikeRepository.deleteById(id);
  }
}
