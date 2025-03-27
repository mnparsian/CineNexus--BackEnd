package com.cinenexus.backend.dto.review;

import com.cinenexus.backend.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewResponseDTO {
    private Long id;
    private Long userId;
    private Long mediaId;
    private String mediaTitle;
    private String mediaPosterUrl;
    private String mediaOverview;
    private String content;
    private Double rating;
    private LocalDateTime createdAt;
    private List<Long> likeIds;
    private LocalDateTime updatedAt;
}
