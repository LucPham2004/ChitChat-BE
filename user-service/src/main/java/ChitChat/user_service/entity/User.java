package ChitChat.user_service.entity;

import java.time.Instant;
import java.util.Set;

import ChitChat.user_service.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    private Instant dob; // date of birth

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String bio;

    private String location;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String createdBy;

    private String updatedBy;

    @Column(name = "created_at", nullable = true, updatable = true)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    private String otp;
    private Instant otpGeneratedTime;
    private boolean isActive;



    // Entity relationships

    // Authorities Many-to-Many
    private Set<String> authorityIds;
    
    // Friendships
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Friendship> sentFriendRequests;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Friendship> receivedFriendRequests;

    // Conversations Many-to-Many
    private Set<Long> conversationIds;

    // Message One-to-Many
    private Set<Long> messageIds;

    // Message-Reaction One-to-Many
    private Set<Long> messageReactionIds;

    // Notification One-to-Many
    private Set<Long> notificationIds;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }

}
