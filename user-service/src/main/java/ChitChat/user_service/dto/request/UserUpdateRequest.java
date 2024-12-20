package ChitChat.user_service.dto.request;

import java.time.Instant;

import ChitChat.user_service.enums.Gender;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;

    private String firstName;

    private String lastName;

    private Instant dob; // date of birth

    private Gender gender;

    private String bio;

    private String location;
    
    private String avatarUrl;
}
