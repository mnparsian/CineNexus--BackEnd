package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.commentReview.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    public List<Comment> findByReviewId(Long reviewId);
}
