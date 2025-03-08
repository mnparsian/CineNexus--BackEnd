package com.cinenexus.backend.model.user;

import com.cinenexus.backend.enumeration.FriendRequestStatusType;
import com.cinenexus.backend.enumeration.FriendshipStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "friendship_statuses")
public class FriendshipStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private FriendshipStatusType name;

}
