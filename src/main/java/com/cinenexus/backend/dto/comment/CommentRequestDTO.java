package com.cinenexus.backend.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentRequestDTO {
    private Long userId;
    private Long reviewId;
    private Long mediaId;
    private Long parentCommentId;
    private String content;

}
