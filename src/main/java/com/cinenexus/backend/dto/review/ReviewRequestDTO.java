package com.cinenexus.backend.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewRequestDTO {
    private Long userId;
    private Long mediaId;
    private String content;
    private Double rating;
}
