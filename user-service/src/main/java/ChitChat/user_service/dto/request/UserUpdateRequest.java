package ChitChat.user_service.dto.request;

import ChitChat.user_service.enums.Gender;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;
    
    private String email;

    private String firstName;

    private String lastName;

    private String dob; // date of birth

    private Gender gender;

    private String bio;

    private String location;
    
    private String job;

}
