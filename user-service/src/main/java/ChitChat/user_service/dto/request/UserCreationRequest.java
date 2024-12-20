package ChitChat.user_service.dto.request;

import java.time.Instant;

import ChitChat.user_service.entity.Gender;

import jakarta.validation.constraints.Size;
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
public class UserCreationRequest {
    
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;
    
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String email;
    String firstName;
    String lastName;
    String phone;
    Instant dob;

    Gender gender;

}
