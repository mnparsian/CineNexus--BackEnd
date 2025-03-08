package com.cinenexus.backend.dto.chat;

import com.cinenexus.backend.enumeration.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponseDTO {
    private Long id;
    private String name;
    private ChatRoomType type;
    private Long creatorId;
}
