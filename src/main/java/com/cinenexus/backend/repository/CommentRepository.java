package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.commentReview.Comment;
import com.cinenexus.backend.model.commentReview.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(value = "SELECT * FROM comments WHERE id = :id", nativeQuery = true)
    Optional<Comment> findByIdNative(@Param("id") Long id);


    public List<Comment> findByReviewId(Long reviewId);
    public void deleteAllByReview(Review review);
}
