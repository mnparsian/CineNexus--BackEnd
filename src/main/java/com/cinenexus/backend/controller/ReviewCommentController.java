package com.cinenexus.backend.controller;


import com.cinenexus.backend.dto.comment.CommentRequestDTO;
import com.cinenexus.backend.dto.comment.CommentResponseDTO;
import com.cinenexus.backend.dto.commentlike.CommentLikeResponseDTO;
import com.cinenexus.backend.dto.review.ReviewLikeResponseDTO;
import com.cinenexus.backend.dto.review.ReviewRequestDTO;
import com.cinenexus.backend.dto.review.ReviewResponseDTO;
import com.cinenexus.backend.dto.user.UserResponseDTO;
import com.cinenexus.backend.model.commentReview.*;
import com.cinenexus.backend.service.ReviewCommentService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews-comments")
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;

    public ReviewCommentController(ReviewCommentService reviewCommentService) {
        this.reviewCommentService = reviewCommentService;
    }

    // Review Endpoints
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO review) {
        System.out.println("Received review: " + review);
        return ResponseEntity.ok(reviewCommentService.createReview(review));
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        ReviewResponseDTO review = reviewCommentService.getReviewById(id);
        return ResponseEntity.ok(review);
    }
    @GetMapping("/reviews/all/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviewsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(reviewCommentService.getAllReviewsByUserId(userId));
    }

    @GetMapping("/reviews/media/{mediaId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByMedia(@PathVariable Long mediaId) {
        return ResponseEntity.ok(reviewCommentService.getReviewsByMedia(mediaId));
    }
    @GetMapping("/reviews/writer/{id}")
    public ResponseEntity<UserResponseDTO> getwriterByReviewId(@PathVariable Long id){
        return ResponseEntity.ok(reviewCommentService.getWriterByReviewId(id));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long id, @RequestBody ReviewRequestDTO review) {
        return ResponseEntity.ok(reviewCommentService.updateReview(id, review.getContent(), review.getRating()));
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        reviewCommentService.deleteReview(id,userDetails);
        return ResponseEntity.noContent().build();
    }

    // Comment Endpoints
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO comment) {
        return ResponseEntity.ok(reviewCommentService.createComment(comment));
    }

    @GetMapping("/comments/review/{reviewId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewCommentService.getCommentsByReview(reviewId));
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long id, @RequestBody CommentRequestDTO comment, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(reviewCommentService.updateComment(id, comment.getContent(),userDetails));
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        reviewCommentService.deleteComment(id,userDetails);
        return ResponseEntity.noContent().build();
    }

    // Review Like Endpoints
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/reviews/{reviewId}/like")
    public ResponseEntity<ReviewLike> likeReview(@PathVariable Long reviewId, @RequestParam Long userId) {
        return ResponseEntity.ok(reviewCommentService.likeReview(reviewId, userId));
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/reviews/likes/{id}")
    public ResponseEntity<Void> unlikeReview(@PathVariable Long id) {
        reviewCommentService.unlikeReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reviews/like/{reviewId}")
    public ResponseEntity<List<ReviewLikeResponseDTO>> getAllLikeByReviewId(@PathVariable Long reviewId){
        List<ReviewLikeResponseDTO> reviewLikeResponseDTO= reviewCommentService.getAllLikesByReview_Id(reviewId);
        return ResponseEntity.ok(reviewLikeResponseDTO);
    }

    // Comment Like Endpoints
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<CommentLikeResponseDTO> likeComment(@PathVariable Long commentId, @RequestParam Long userId) {
        return ResponseEntity.ok(reviewCommentService.likeComment(commentId, userId));
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/comments/likes/{id}")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long id) {
        reviewCommentService.unlikeComment(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/comments/{commentId}/isLiked")
    public ResponseEntity<Boolean> isCommentLikedByUser(@PathVariable Long commentId,@RequestParam Long userId){
        boolean isliked = reviewCommentService.isCommentLikedByUser(commentId,userId);
        return ResponseEntity.ok(isliked);
    }
    @GetMapping("/comments/{commentId}/likes")
    public ResponseEntity<List<CommentLikeResponseDTO>> findAllCommentLikesByCommentId(@PathVariable Long commentId){
        return ResponseEntity.ok(reviewCommentService.findCommentLikesByCommentId(commentId));
    }
}

