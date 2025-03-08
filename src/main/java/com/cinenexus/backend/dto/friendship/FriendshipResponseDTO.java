package com.cinenexus.backend.dto.friendship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendshipResponseDTO {
    private Long id;
    private Long userId; // کاربری که درخواست داده
    private Long friendId; // کاربری که درخواست رو گرفته
    private String requestStatus;
    private String friendshipStatus;
}
