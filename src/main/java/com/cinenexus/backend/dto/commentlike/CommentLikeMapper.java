package com.cinenexus.backend.dto.commentlike;

import com.cinenexus.backend.model.commentReview.CommentLike;

public class CommentLikeMapper {

    public CommentLikeResponseDTO toDTO(CommentLike commentLike){
        CommentLikeResponseDTO dto = new CommentLikeResponseDTO();
    dto.setId(commentLike.getId());
    dto.setUserId(commentLike.getUser().getId());
    dto.setCommentId(commentLike.getComment().getId());
    dto.setLikedAt(commentLike.getLikedAt());
    return dto;
    }
}
