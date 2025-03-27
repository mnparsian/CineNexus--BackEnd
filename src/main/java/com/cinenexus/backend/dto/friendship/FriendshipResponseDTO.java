package com.cinenexus.backend.dto.friendship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendshipResponseDTO {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String surname;
    private String profileUrl;
    private Long friendId;
    private String friendUsername;
    private String friendName;
    private String friendSurname;
    private String friendProfileurl;
    private String requestStatus;
    private String friendshipStatus;
}
