package ChitChat.user_service.mapper;

import org.springframework.stereotype.Component;

import ChitChat.user_service.dto.response.FriendShipResponse;
import ChitChat.user_service.entity.Friendship;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class FriendshipMapper {
    
    public FriendShipResponse toFriendShipResponse(Friendship friendship) {
        FriendShipResponse response = new FriendShipResponse();
        response.setFriendshipId(friendship.getId());
        response.setReceiverId(friendship.getRecipient().getId());
        response.setRequesterId(friendship.getSender().getId());
        response.setStatus(friendship.getStatus());

        return response;
    }
}
