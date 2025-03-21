package ChitChat.user_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ChitChat.user_service.entity.Friendship;
import ChitChat.user_service.enums.FriendshipStatus;
import ChitChat.user_service.exception.AppException;
import ChitChat.user_service.exception.ErrorCode;
import ChitChat.user_service.repository.FriendshipRepository;
import ChitChat.user_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendshipService {

    FriendshipRepository friendShipRepository;
    UserService userService;
    UserRepository userRepository;
    
    public Friendship getFriendStatus(Long requesterId, Long receiverId) {
        if (!userRepository.existsById(requesterId) || !userRepository.existsById(receiverId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Friendship friendship = friendShipRepository.findBy2UserIds(requesterId, receiverId);

        return friendship;
    }

    public Friendship sendFriendRequest(Long requesterId, Long receiverId) {
        if (!userRepository.existsById(requesterId) || !userRepository.existsById(receiverId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Friendship friendship = friendShipRepository.findBy2UserIds(requesterId, receiverId);
        if(friendship != null) {
            return friendship;
        }
        friendship = new Friendship();
        friendship.setSender(userService.findById(requesterId).get());
        friendship.setRecipient(userService.findById(receiverId).get());
        friendship.setStatus(FriendshipStatus.Pending);

        return friendShipRepository.save(friendship);
    }

    public void deleteFriendShip(Long iselfId, Long otherId) {
        Friendship friendship = friendShipRepository.findBy2UserIds(iselfId, otherId);
        if (friendship == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        friendShipRepository.delete(friendship);
    }

    @Transactional
    public Friendship editFriendShipStatus(Long selfId, Long otherId, FriendshipStatus status) {
        Friendship friendship = friendShipRepository.findBy2UserIds(selfId, otherId);

        friendship.setStatus(status);

        return friendShipRepository.save(friendship);
    }
}
