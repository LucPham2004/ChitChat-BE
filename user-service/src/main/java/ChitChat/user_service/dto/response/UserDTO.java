package ChitChat.user_service.dto.response;

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
public class UserDTO {
    Long id;
    String firstName;
    String lastName;
    String location;
    String job;
    String avatarPublicId;
    String avatarUrl;
    boolean isFriend;
    boolean isFriendRequestSent;
    int friendNum;
    Long mutualFriendsNum;

    Long conversationId;
}
