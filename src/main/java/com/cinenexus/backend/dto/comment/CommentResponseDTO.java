package com.cinenexus.backend.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponseDTO {
    private Long id;
    private Long userId;
    private String username;
    private String profileImage;
    private Long reviewId;
    private Long mediaId;
    private Long parentCommentId;
    private List<Long> repliesIds;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
