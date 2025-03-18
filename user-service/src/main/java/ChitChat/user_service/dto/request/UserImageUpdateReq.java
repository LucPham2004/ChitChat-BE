package ChitChat.user_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserImageUpdateReq {

    private Long id;
    private String avatarPublicId;
    private String avatarUrl;
    private String coverPhotoPublicId;
    private String coverPhotoUrl;
}
