package com.cinenexus.backend.dto.chat;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatReactionDTO {
    private Long id;
    private Long messageId;
    private Long userId;
    private String reactionType;
}

