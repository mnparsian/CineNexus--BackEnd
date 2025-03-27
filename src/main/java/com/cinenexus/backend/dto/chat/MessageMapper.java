package com.cinenexus.backend.dto.chat;

import com.cinenexus.backend.model.chat.ChatMessage;
import lombok.Data;



@Data
public class MessageMapper {
    public MessageResponseDTO toDTO(ChatMessage message) {
        return new MessageResponseDTO(
                message.getId(),
                message.getChatRoom().getId(),
                message.getSender().getId(),
                message.getContent(),
                message.getSentAt(),
                message.getEditedAt(),
                message.getDeletedAt()

        );
    }
}

