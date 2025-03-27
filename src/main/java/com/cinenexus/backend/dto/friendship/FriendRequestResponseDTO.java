package com.cinenexus.backend.dto.friendship;
import com.cinenexus.backend.model.user.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendRequestResponseDTO {

  private Long id;
  private Long senderId;
  private Long receiverId;
  private String status;
  private LocalDate sentAt;

  public FriendRequestResponseDTO toDTO(FriendRequest friendRequest){
    FriendRequestResponseDTO dto = new FriendRequestResponseDTO();
    dto.setId(friendRequest.getId());
    dto.setSenderId(friendRequest.getSender().getId());
    dto.setReceiverId(friendRequest.getReceiver().getId());
    dto.setStatus(friendRequest.getStatus().getName().name());
    dto.setSentAt(friendRequest.getSentAt());
    return dto;
  }
}
