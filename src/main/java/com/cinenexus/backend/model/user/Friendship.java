package com.cinenexus.backend.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-friends")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    @JsonBackReference(value = "user-friends")
    private User friend;

    @ManyToOne
    @JoinColumn(name = "request_status_id", nullable = false)
    private FriendRequestStatus requestStatus; // وضعیت درخواست دوستی (PENDING, ACCEPTED, REJECTED)

    @ManyToOne
    @JoinColumn(name = "friendship_status_id", nullable = true)
    private FriendshipStatus friendshipStatus; // وضعیت دوستی (ACCEPTED, BLOCKED)

}
