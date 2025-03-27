package com.cinenexus.backend.service;

import com.cinenexus.backend.dto.comment.CommentMapper;
import com.cinenexus.backend.dto.comment.CommentRequestDTO;
import com.cinenexus.backend.dto.comment.CommentResponseDTO;
import com.cinenexus.backend.dto.commentlike.CommentLikeMapper;
import com.cinenexus.backend.dto.commentlike.CommentLikeResponseDTO;
import com.cinenexus.backend.dto.review.ReviewLikeResponseDTO;
import com.cinenexus.backend.dto.review.ReviewMapper;
import com.cinenexus.backend.dto.review.ReviewRequestDTO;
import com.cinenexus.backend.dto.review.ReviewResponseDTO;
import com.cinenexus.backend.dto.user.UserMapper;
import com.cinenexus.backend.dto.user.UserResponseDTO;
import com.cinenexus.backend.enumeration.RoleType;
import com.cinenexus.backend.model.commentReview.*;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ReviewCommentService {

  private final ReviewRepository reviewRepository;
  private final CommentRepository commentRepository;
  private final ReviewLikeRepository reviewLikeRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final UserRepository userRepository;
  private final MediaRepository mediaRepository;
  private final ReviewMapper reviewMapper;
  private final CommentMapper commentMapper;
  private final CommentLikeMapper commentLikeMapper;
  private final UserMapper userMapper;
  private final ReviewLikeResponseDTO reviewLikeResponseDTO;


  public ReviewCommentService(
      ReviewRepository reviewRepository,
      CommentRepository commentRepository,
      ReviewLikeRepository reviewLikeRepository,
      CommentLikeRepository commentLikeRepository,
      UserRepository userRepository,
      MediaRepository mediaRepository,
      ReviewMapper reviewMapper,
      CommentMapper commentMapper,
      CommentLikeMapper commentLikeMapper,
      UserMapper userMapper,ReviewLikeResponseDTO reviewLikeResponseDTO) {
    this.reviewRepository = reviewRepository;
    this.commentRepository = commentRepository;
    this.reviewLikeRepository = reviewLikeRepository;
    this.commentLikeRepository = commentLikeRepository;
    this.userRepository = userRepository;
    this.mediaRepository = mediaRepository;
    this.reviewMapper = reviewMapper;
    this.commentMapper = commentMapper;
    this.commentLikeMapper = commentLikeMapper;
    this.userMapper = userMapper;
    this.reviewLikeResponseDTO = reviewLikeResponseDTO;
  }

  // Review Methods
  public ReviewResponseDTO createReview(ReviewRequestDTO dto) {

    Review review = reviewMapper.toEntity(dto);

    review = reviewRepository.save(review);

    return reviewMapper.toDTO(review);
  }

  public ReviewResponseDTO getReviewById(Long id) {
    Review review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review Not found"));
    return reviewMapper.toDTO(review);

  }

  public List<ReviewResponseDTO> getAllReviewsByUserId(Long userId){
    User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("user not found"));
    List<Review> reviews = reviewRepository.findAllByUser(user);
    return reviews.stream().map(reviewMapper::toDTO).toList();
  }

  public List<ReviewResponseDTO> getReviewsByMedia(Long mediaId) {
    List<Review> reviews = reviewRepository.findByMediaId(mediaId);
    return reviews.stream().map(reviewMapper::toDTO).toList();
  }
  public UserResponseDTO getWriterByReviewId(Long id){
    Review review = reviewRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Review not found"));
    User writer = review.getUser();
    return userMapper.toDTO(writer);
  }

  public ReviewResponseDTO updateReview(Long id, String content, Double rating) {
    return reviewRepository
        .findById(id)
        .map(
            review -> {
              review.setContent(content);
              review.setRating(rating);
              review.setUpdatedAt(LocalDateTime.now());
              Review updatedReview = reviewRepository.save(review);
              return reviewMapper.toDTO(updatedReview);
            })
        .orElseThrow(() -> new RuntimeException("Review not found"));
  }

  @Transactional
  public void deleteReview(Long id, UserDetails userDetails) {
    User currentUser = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review not found"));


    if (currentUser.getRole().name().equals("ADMIN")) {
      commentRepository.deleteAllByReview(review);
      reviewRepository.deleteById(id);
      return;
    }


    if (!currentUser.getId().equals(review.getUser().getId())) {
      throw new AccessDeniedException("You are not allowed to delete this review.");
    }
    commentRepository.deleteAllByReview(review);
    reviewRepository.delete(review);
  }

  //find all likes By review id

  public List<ReviewLikeResponseDTO> getAllLikesByReview_Id(Long reviewId){
    List<ReviewLike> reviewLikes = reviewLikeRepository.findByReview_Id(reviewId);
    return reviewLikes.stream().map(reviewLikeResponseDTO::toDTO).toList();
  }



  // Comment Methods
  public CommentResponseDTO createComment(CommentRequestDTO dto) {
   Comment comment = commentMapper.toEntity(dto);
    comment = commentRepository.save(comment);
    return commentMapper.toDTO(comment);
  }

  public List<CommentResponseDTO> getCommentsByReview(Long reviewId) {
   List <Comment> foundComment = commentRepository.findByReviewId(reviewId);
    return foundComment.stream().map(commentMapper::toDTO).toList();
  }

  public Comment getCommentById(Long id){
    return commentRepository.findByIdNative(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));

  }
  @Transactional
  public CommentResponseDTO updateComment(long id, String content, UserDetails userDetails) {

    Optional<Comment> optionalComment = commentRepository.findById(id);
    if(optionalComment.isEmpty()) {
      throw new RuntimeException("Comment with id " + id + " not found in database!");
    }
    Comment updatedComment = optionalComment.get();
    User currentUser = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!updatedComment.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().name().equals("ADMIN")) {
      throw new AccessDeniedException("You are not allowed to edit this comment.");
    }

    updatedComment.setContent(content);
    updatedComment.setUpdatedAt(LocalDateTime.now());
    return commentMapper.toDTO(updatedComment);
  }

@Transactional
  public void deleteComment(Long id, UserDetails userDetails) {

    User currentUser = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("comment not found"));


    if (currentUser.getRole().name().equals("ADMIN")) {
      commentLikeRepository.deleteByCommentId(id);
      commentRepository.deleteById(id);
      return;
    }


    if (!currentUser.getId().equals(comment.getUser().getId())) {
      throw new AccessDeniedException("You are not allowed to delete this comment.");
    }
  commentLikeRepository.deleteByCommentId(id);
    commentRepository.delete(comment);
  }

  // Review Like Methods
  public ReviewLike likeReview(Long reviewId, Long userId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    Review review =
        reviewRepository
            .findById(reviewId)
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
  public CommentLikeResponseDTO likeComment(Long commentId, Long userId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    Comment comment =
        commentRepository
            .findById(commentId)
            .orElseThrow(() -> new RuntimeException("Comment not found"));

    CommentLike commentLike = new CommentLike();
    commentLike.setUser(user);
    commentLike.setComment(comment);
    CommentLike savedCommentLike = commentLikeRepository.save(commentLike);
    return commentLikeMapper.toDTO(savedCommentLike);
  }

  public void unlikeComment(Long id) {
    commentLikeRepository.deleteById(id);
  }

  public boolean isCommentLikedByUser(Long commentId,Long userId){
    Comment comment = commentRepository.findById(commentId).orElseThrow(()->new EntityNotFoundException("Comment not found"));
    User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found"));
    return commentLikeRepository.existsByCommentAndUser(comment,user);
  }

  public List<CommentLikeResponseDTO> findCommentLikesByCommentId(Long commentId){
    Comment comment = commentRepository.findById(commentId).orElseThrow(()->new EntityNotFoundException("Comment not found"));
   List<CommentLike> commentLikeList = commentLikeRepository.findByComment(comment);
   return commentLikeList.stream().map(commentLikeMapper::toDTO).toList();
  }
}
