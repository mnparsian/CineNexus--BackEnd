package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.user.Friendship;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    boolean existsByUserAndFriend(User user, User friend);
    Optional<Friendship> findByUserAndFriend(User user, User friend);
    Page<Friendship> findAllByUserOrFriend(User user, User friend, Pageable pageable);
    @Query("SELECT f FROM Friendship f WHERE (f.user = :user1 AND f.friend = :user2) OR (f.user = :user2 AND f.friend = :user1)")
    Optional<Friendship> findByUsers(@Param("user1") User user1, @Param("user2") User user2);

    public boolean existsByUserIdAndFriendId(Long userId,Long friendId);

    List<Friendship> findAllByUser(User user);


}
