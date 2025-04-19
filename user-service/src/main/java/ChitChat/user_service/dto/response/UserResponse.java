package ChitChat.user_service.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import ChitChat.user_service.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class UserResponse {
     Long id;
     String email;
     String username;
     String firstName;
     String lastName;
     String location;
     String bio;
     String job;
     Set<String> authorities;
     String phone;
     
     String avatarPublicId;
     String avatarUrl;
     String coverPhotoPublicId;
     String coverPhotoUrl;

     Long conversationId;

     // social media links
     String facebook;
     String twitter;
     String instagram;
     String linkedin;
     String youtube;
     String github;
     String tiktok;
     String discord;

     LocalDate dob;
     Instant createdAt;
     Instant updatedAt;
     
     Gender gender;

     boolean isFriend;

     int friendNum;
     Long mutualFriendsNum;
}
