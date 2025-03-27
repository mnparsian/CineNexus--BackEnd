package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.chat.ChatRoom;
import com.cinenexus.backend.model.chat.UserChatRoom;
import com.cinenexus.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom,Long> {
    @Query("SELECT ucr.chatRoom FROM UserChatRoom ucr WHERE ucr.user = :user")
    List<ChatRoom> findChatRoomsByUser(@Param("user") User user);

    List<UserChatRoom> findUsersByChatRoom(ChatRoom chatRoom);

    @Query("SELECT COUNT(ucr) FROM UserChatRoom ucr WHERE ucr.chatRoom.id = :chatRoomId")
    int countUsersInChatRoom(@Param("chatRoomId") Long chatRoomId);


    @Query("SELECT ucr FROM UserChatRoom ucr WHERE ucr.chatRoom.type = 'PRIVATE' AND ucr.user != :sender")
    Optional<UserChatRoom> findReceiverInPrivateChat(@Param("sender") User sender);

}
