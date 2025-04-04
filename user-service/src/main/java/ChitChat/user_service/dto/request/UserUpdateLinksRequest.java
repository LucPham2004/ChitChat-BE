package ChitChat.user_service.dto.request;

import lombok.Data;

@Data
public class UserUpdateLinksRequest {
    private Long id;
    
    private String facebook;
    private String twitter;
    private String instagram;
    private String linkedin;
    private String youtube;
    private String github;
    private String tiktok;
    private String discord;
}
