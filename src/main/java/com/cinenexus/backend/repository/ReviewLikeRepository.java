package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.commentReview.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike,Long> {}
