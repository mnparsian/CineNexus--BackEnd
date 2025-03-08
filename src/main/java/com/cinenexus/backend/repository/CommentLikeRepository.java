package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.commentReview.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {}
