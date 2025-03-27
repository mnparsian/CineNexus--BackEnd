package com.cinenexus.backend.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatSocket {
    private Long chatroomId;
    private Long senderId;
    private Long receiverId;
    private String content;
}
