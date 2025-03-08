package com.cinenexus.backend.repository;

import com.cinenexus.backend.enumeration.FriendRequestStatusType;
import com.cinenexus.backend.enumeration.FriendshipStatusType;
import com.cinenexus.backend.model.user.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipStatusRepository extends JpaRepository<FriendshipStatus,Long> {
    public Optional<FriendshipStatus> findByName(FriendshipStatusType name);
}
