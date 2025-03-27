package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.commentReview.Review;
import com.cinenexus.backend.model.commentReview.ReviewLike;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    public List<Review> findByMediaId(Long mediaId);
    public List<Review> findAllByUser(User user);
}
