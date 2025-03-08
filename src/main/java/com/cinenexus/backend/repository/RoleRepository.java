package com.cinenexus.backend.repository;

import com.cinenexus.backend.enumeration.RoleType;
import com.cinenexus.backend.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    public Optional<Role> findByName(RoleType name);
}
