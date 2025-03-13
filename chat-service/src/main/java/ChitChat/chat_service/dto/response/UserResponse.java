package ChitChat.chat_service.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import ChitChat.chat_service.enums.Gender;
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
     String avatarPublicId;
     String avatarUrl;
     String firstName;
     String lastName;
     String location;
     String bio;
     String job;
     Set<String> authorities;
     String phone;

     LocalDate dob;
     Instant createdAt;
     Instant updatedAt;
     
     Gender gender;

     int friendNum;
     Long mutualFriendsNum;
}
