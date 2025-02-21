package ChitChat.auth_service.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import ChitChat.auth_service.entity.Gender;
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

     String username;
     String email;

     Set<String> authorities;

     String firstName;
     String lastName;
     String phone;
     String avatarUrl;

     LocalDate dob;
     Instant createdAt;
     Instant updatedAt;
     
     Gender gender;

     int friendNum;
     Long mutualFriendsNum;
}
