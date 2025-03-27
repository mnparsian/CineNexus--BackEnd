package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//  User findByUsername(String username);
  User findByEmail(String email);
  public Optional<User> findByUsername(String username);

  List<User> findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(String username, String name, String surname);

}
