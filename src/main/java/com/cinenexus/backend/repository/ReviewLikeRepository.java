package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.commentReview.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike,Long> {
    public List<ReviewLike> findByReview_Id(Long id);
}
