package com.cinenexus.backend.dto.friendship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendshipRequestDTO {
    private Long friendId; // آیدی کاربری که درخواست دوستی بهش فرستاده می‌شه
}
