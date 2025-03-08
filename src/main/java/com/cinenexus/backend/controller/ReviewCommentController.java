package com.cinenexus.backend.controller;


import com.cinenexus.backend.model.commentReview.*;
import com.cinenexus.backend.service.ReviewCommentService;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/reviews")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewCommentService.createReview(review));
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        return reviewCommentService.getReviewById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reviews/media/{mediaId}")
    public ResponseEntity<List<Review>> getReviewsByMedia(@PathVariable Long mediaId) {
        return ResponseEntity.ok(reviewCommentService.getReviewsByMedia(mediaId));
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        return ResponseEntity.ok(reviewCommentService.updateReview(id, review.getContent(), review.getRating()));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewCommentService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    // Comment Endpoints
    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(reviewCommentService.createComment(comment));
    }

    @GetMapping("/comments/review/{reviewId}")
    public ResponseEntity<List<Comment>> getCommentsByReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewCommentService.getCommentsByReview(reviewId));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        return ResponseEntity.ok(reviewCommentService.updateComment(id, comment.getContent()));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        reviewCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    // Review Like Endpoints
    @PostMapping("/reviews/{reviewId}/like")
    public ResponseEntity<ReviewLike> likeReview(@PathVariable Long reviewId, @RequestParam Long userId) {
        return ResponseEntity.ok(reviewCommentService.likeReview(reviewId, userId));
    }

    @DeleteMapping("/reviews/likes/{id}")
    public ResponseEntity<Void> unlikeReview(@PathVariable Long id) {
        reviewCommentService.unlikeReview(id);
        return ResponseEntity.noContent().build();
    }

    // Comment Like Endpoints
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<CommentLike> likeComment(@PathVariable Long commentId, @RequestParam Long userId) {
        return ResponseEntity.ok(reviewCommentService.likeComment(commentId, userId));
    }

    @DeleteMapping("/comments/likes/{id}")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long id) {
        reviewCommentService.unlikeComment(id);
        return ResponseEntity.noContent().build();
    }
}

