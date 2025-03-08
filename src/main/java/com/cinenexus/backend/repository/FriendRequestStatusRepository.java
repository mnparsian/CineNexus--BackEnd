package com.cinenexus.backend.repository;

import com.cinenexus.backend.enumeration.FriendRequestStatusType;
import com.cinenexus.backend.model.user.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestStatusRepository extends JpaRepository<FriendRequestStatus,Long> {
    public Optional<FriendRequestStatus> findByName(FriendRequestStatusType name);
}
