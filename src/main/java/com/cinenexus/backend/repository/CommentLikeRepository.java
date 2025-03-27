package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.commentReview.Comment;
import com.cinenexus.backend.model.commentReview.CommentLike;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    boolean existsByCommentAndUser(Comment comment, User user);
    void deleteByCommentId(Long commentId);
    List<CommentLike> findByComment(Comment comment);
}
