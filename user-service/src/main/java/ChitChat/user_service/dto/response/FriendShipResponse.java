package ChitChat.user_service.dto.response;

import ChitChat.user_service.enums.FriendshipStatus;
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
public class FriendShipResponse {
    Long friendshipId;
    Long requesterId;
    Long receiverId;

    FriendshipStatus status;
}
