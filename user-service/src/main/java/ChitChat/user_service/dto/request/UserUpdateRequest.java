package ChitChat.user_service.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;
    
    private String email;

    private String firstName;

    private String lastName;

    private String dob; // date of birth

    private String gender;

    private String bio;

    private String location;
    
    private String job;

}
