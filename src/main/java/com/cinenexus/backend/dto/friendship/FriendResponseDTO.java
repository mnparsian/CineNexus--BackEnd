package com.cinenexus.backend.dto.friendship;

import com.cinenexus.backend.model.user.Friendship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendResponseDTO {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String surname;
    private String profileUrl;
    private String requestStatus;
    private String friendshipStatus;

    public FriendResponseDTO toDTO(Friendship friendship){
        FriendResponseDTO dto = new FriendResponseDTO();
    dto.setId(friendship.getId());
    dto.setUserId(friendship.getFriend().getId());
    dto.setUsername(friendship.getFriend().getUsername());
    dto.setName(friendship.getFriend().getName());
    dto.setSurname(friendship.getFriend().getSurname());
    dto.setProfileUrl(friendship.getFriend().getProfileImage());
    dto.setRequestStatus(friendship.getRequestStatus().getName().name());
    dto.setFriendshipStatus(friendship.getFriendshipStatus().getName().name());
    return dto;
    }
}
