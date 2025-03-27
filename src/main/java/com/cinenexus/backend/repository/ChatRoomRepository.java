package com.cinenexus.backend.repository;

import com.cinenexus.backend.enumeration.ChatRoomType;
import com.cinenexus.backend.model.chat.ChatRoom;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    @Query("SELECT c FROM ChatRoom c WHERE c.type = :type AND c.createdBy = :creator AND c.id IN " +
            "(SELECT uc.chatRoom.id FROM UserChatRoom uc WHERE uc.user.id IN :userIds " +
            "GROUP BY uc.chatRoom.id HAVING COUNT(uc.chatRoom.id) = :#{#userIds.size()})")
    Optional<ChatRoom> findByParticipantsAndTypeAndCreator(@Param("userIds") List<Long> userIds,
                                                           @Param("type") ChatRoomType type,
                                                           @Param("creator") User creator);



    @Query("SELECT cr FROM ChatRoom cr " +
            "JOIN cr.participants p1 " +
            "JOIN cr.participants p2 " +
            "WHERE p1.user = :user1 " +
            "AND p2.user = :user2 " +
            "AND cr.type = 'PRIVATE'")
    Optional<ChatRoom> findPrivateChatRoomByUsers(@Param("user1") User user1, @Param("user2") User user2);



    @Query("SELECT cr FROM ChatRoom cr JOIN cr.participants p1 JOIN cr.participants p2 " +
            "WHERE p1.user.id = :userId AND p2.user.id = :friendId AND cr.type = 'PRIVATE'")
    Optional<ChatRoom> findPrivateChatRoom(@Param("userId") Long userId, @Param("friendId") Long friendId);

}
