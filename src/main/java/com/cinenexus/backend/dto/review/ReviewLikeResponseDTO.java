package com.cinenexus.backend.dto.review;

import com.cinenexus.backend.model.commentReview.ReviewLike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewLikeResponseDTO {
    private Long id;
    private Long reviewId;
    private Long userId;
    private LocalDateTime likedAt;


    public ReviewLikeResponseDTO toDTO (ReviewLike reviewLike){
        ReviewLikeResponseDTO reviewLikeResponseDTO = new ReviewLikeResponseDTO();
        reviewLikeResponseDTO.setId(reviewLike.getId());
        reviewLikeResponseDTO.setReviewId(reviewLike.getReview().getId());
        reviewLikeResponseDTO.setUserId(reviewLike.getUser().getId());
        reviewLikeResponseDTO.setLikedAt(reviewLike.getLikedAt());

        return reviewLikeResponseDTO;
    }
}
