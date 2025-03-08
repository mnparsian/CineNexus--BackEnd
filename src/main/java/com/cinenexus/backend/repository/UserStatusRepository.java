package com.cinenexus.backend.repository;

import com.cinenexus.backend.enumeration.UserStatusType;
import com.cinenexus.backend.model.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus,Long> {
    Optional<UserStatus> findByName(UserStatusType name);

}
