package com.cinenexus.backend.dto.commentlike;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentLikeResponseDTO {
    private Long id;
    private Long commentId;
    private Long userId;
    private LocalDateTime likedAt;
}
