package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.chat.ChatRoom;
import com.cinenexus.backend.model.chat.ChatSeen;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatSeenRepository extends JpaRepository<ChatSeen,Long> {
    @Query("SELECT cs FROM ChatSeen cs WHERE cs.user.id = :userId AND cs.message.chatRoom.id = :chatRoomId")
    Optional<ChatSeen> findByUserIdAndChatRoom(@Param("userId") Long userId, @Param("chatRoomId") Long chatRoomId);


    @Query("SELECT cs FROM ChatSeen cs WHERE cs.user = :user AND cs.message.chatRoom = :chatRoom")
    Optional<ChatSeen> findByUserAndChatRoom(@Param("user") User user, @Param("chatRoom") ChatRoom chatRoom);

}
