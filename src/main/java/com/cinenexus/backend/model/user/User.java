package com.cinenexus.backend.model.user;

import com.cinenexus.backend.model.chat.ChatMessage;
import com.cinenexus.backend.model.chat.ChatReaction;
import com.cinenexus.backend.model.chat.ChatSeen;
import com.cinenexus.backend.model.chat.UserChatRoom;
import com.cinenexus.backend.model.commentReview.Comment;
import com.cinenexus.backend.model.media.FavoriteMovie;
import com.cinenexus.backend.model.commentReview.Review;
import com.cinenexus.backend.model.misc.Country;
import com.cinenexus.backend.model.misc.Language;
import com.cinenexus.backend.model.payment.Payment;
import com.cinenexus.backend.model.payment.Subscription;
import com.cinenexus.backend.model.whatchlist.UserWatchlist;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @ManyToOne
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  private String name;
  private String surname;
  private String bio;
  private String profileImage;

  private Boolean isVerified = false;
  private LocalDateTime lastLogin;

  @ManyToOne
  @JoinColumn(name = "status_id", nullable = false)
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  private LocalDateTime createdAt = LocalDateTime.now();


  private LocalDate birthday;

  @ManyToOne
  @JoinColumn(name = "country_id")
  private Country country;

  @ManyToOne
  @JoinColumn(name = "preferred_language_id")
  private Language preferredLanguage;

  private String phoneNumber;

  @ElementCollection(fetch = FetchType.LAZY)
  private List<String> socialLinks = new ArrayList<>();




  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference(value = "user-friends")
  private List<Friendship> friends = new ArrayList<>();


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<FavoriteMovie> favoriteMovies = new ArrayList<>();


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference(value = "user-reviews")
  private List<Review> reviews = new ArrayList<>();


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference(value = "user-comments")
  private List<Comment> comments = new ArrayList<>();


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference(value = "user-watchlist")
  private List<UserWatchlist> watchlist = new ArrayList<>();


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference(value = "user-chatRooms")
  private List<UserChatRoom> chatRooms = new ArrayList<>();


  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private List<ChatMessage> messages = new ArrayList<>();


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<ChatSeen> lastSeenMessages = new ArrayList<>();


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<ChatReaction> reactions = new ArrayList<>();


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Subscription> subscriptions = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Payment> payments = new ArrayList<>();


    public User(Long senderId) {

    }
}
