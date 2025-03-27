package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.user.FriendRequest;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {
    List<FriendRequest> findAllBySender(User sender);
}
