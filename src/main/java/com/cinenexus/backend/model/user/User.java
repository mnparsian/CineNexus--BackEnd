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

  // اطلاعات اضافی
  private LocalDate birthday;

  @ManyToOne
  @JoinColumn(name = "country_id")
  private Country country;

  @ManyToOne
  @JoinColumn(name = "preferred_language_id")
  private Language preferredLanguage;

  private String phoneNumber;

  @ElementCollection
  private List<String> socialLinks;

  // روابط

  // لیست دوستان (با جدول میانی)
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference(value = "user-friends")
  private List<Friendship> friends = new ArrayList<>();

  // لیست فیلم‌های مورد علاقه (با جدول میانی)
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<FavoriteMovie> favoriteMovies = new ArrayList<>();

  // لیست نقدهای کاربر
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Review> reviews = new ArrayList<>();

  // لیست کامنت‌های کاربر
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Comment> comments = new ArrayList<>();

  // لیست تماشای کاربر (ترکیب واچ‌لیست، در حال تماشا، و دیده‌شده)
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<UserWatchlist> watchlist = new ArrayList<>();

  // چت‌ها (با جدول میانی)
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<UserChatRoom> chatRooms = new ArrayList<>();

  // لیست پیام‌های ارسال‌شده
  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private List<ChatMessage> messages = new ArrayList<>();

  // لیست پیام‌هایی که این کاربر خوانده است
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<ChatSeen> lastSeenMessages = new ArrayList<>();

  // لیست ری‌اکشن‌های کاربر به پیام‌ها
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<ChatReaction> reactions = new ArrayList<>();

  // پرداخت‌ها و اشتراک‌ها
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Subscription> subscriptions = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Payment> payments = new ArrayList<>();


    public User(Long senderId) {

    }
}
