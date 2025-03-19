package ChitChat.chat_service.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ChitChat.chat_service.dto.request.ConversationRequest;
import ChitChat.chat_service.dto.response.ConversationResponse;
import ChitChat.chat_service.dto.response.ConversationShortResponse;
import ChitChat.chat_service.dto.response.UserResponse;
import ChitChat.chat_service.entity.Conversation;
import ChitChat.chat_service.entity.Message;
import ChitChat.chat_service.service.UserServiceClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class ConversationMapper {

    //MessageRepository messageRepository;
    MessageMapper messageMapper;
    UserServiceClient userServiceClient;
    
    public Conversation toConversation(ConversationRequest conversationRequest) {
        return Conversation.builder()
            .name(conversationRequest.getName() != null ?
                conversationRequest.getName() : null)
            .description(conversationRequest.getDescription())
            .avatarUrl(conversationRequest.getAvatarUrl() != null && !conversationRequest.isGroup() ? 
                conversationRequest.getAvatarUrl() : null)
            .avatarPublicId(conversationRequest.getAvatarPublicId())
            .color(conversationRequest.getColor())
            .emoji(conversationRequest.getEmoji())
            .participantIds(conversationRequest.getParticipantIds())
            .ownerId(conversationRequest.getOwnerId())
            .isGroup(conversationRequest.isGroup())
            .isRead(conversationRequest.isRead())
            .isMuted(conversationRequest.isMuted())
            .isPinned(conversationRequest.isPinned())
            .isArchived(conversationRequest.isArchived())
            .isDeleted(conversationRequest.isDeleted())
            .isBlocked(conversationRequest.isBlocked())
            .isReported(conversationRequest.isReported())
            .isSpam(conversationRequest.isSpam())
            .isMarkedAsUnread(conversationRequest.isMarkedAsUnread())
            .isMarkedAsRead(conversationRequest.isMarkedAsRead())
            .build();
    }

    public ConversationShortResponse toConversationShortResponse(Conversation conversation, Long userId) {

        // Đặt tên conversation linh hoạt từng góc nhìn
        // Danh sách participant ngoại trừ userId
        List<Long> otherParticipants = conversation.getParticipantIds().stream()
        .filter(id -> !id.equals(userId))
        .collect(Collectors.toList());

        String conversationName;
        if (conversation.isGroup()) {
            // Nếu là group chat -> lấy tên tất cả participants khác userId
            List<String> participantNames = otherParticipants.stream().limit(3)
                .map(id -> userServiceClient.getUserById(id).getResult().getFirstName())
                .collect(Collectors.toList());
            conversationName = String.join(", ", participantNames);
        } else {
            // Nếu không phải group chat -> chỉ lấy tên của 1 người còn lại
            UserResponse user = userServiceClient.getUserById(otherParticipants.get(0)).getResult();
            conversationName = otherParticipants.isEmpty()
                ? "Người dùng không tồn tại"
                : user.getFirstName() + " " + user.getLastName();
        }

        // Tin nhắn cuối
        Set<Message> messages = conversation.getMessages();
        Message lastMessage = null;
        if (messages != null && !messages.isEmpty()) {
            lastMessage = messages.stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .orElse(null);
        }

        // Lấy danh sách avatarUrls
        String defaultAvatar = "/user_default.avif";
        List<String> avatarUrls = new ArrayList<>();

        if (conversation.getAvatarUrl() != null) {
            avatarUrls.add(conversation.getAvatarUrl());
        } else {
            if (conversation.isGroup()) {
                avatarUrls = otherParticipants.stream()
                    .limit(4)
                    .map(id -> {
                        UserResponse user = userServiceClient.getUserById(id).getResult();
                        return user.getAvatarUrl() != null ? user.getAvatarUrl() : defaultAvatar;
                    })
                    .collect(Collectors.toList());
            } else {
                if (!otherParticipants.isEmpty()) {
                    UserResponse user = userServiceClient.getUserById(otherParticipants.get(0)).getResult();
                    avatarUrls.add(user.getAvatarUrl() != null ? user.getAvatarUrl() : defaultAvatar);
                } else {
                    avatarUrls.add(defaultAvatar);
                }
            }
        }

        return ConversationShortResponse.builder()
            .id(conversation.getId())
            .name(conversationName)
            .lastMessage(messageMapper.toResponse(lastMessage))
            .avatarUrls(avatarUrls)
            .avatarPublicId(conversation.getAvatarPublicId())
            .ownerId(conversation.getOwnerId())
            .participantIds(conversation.getParticipantIds())
            .isGroup(conversation.isGroup())
            .isRead(conversation.isRead())
            .build();
    }

    public ConversationResponse toConversationResponse(Conversation conversation, Long userId) {

        // Đặt tên conversation linh hoạt từng góc nhìn
        // Danh sách participant ngoại trừ userId
        List<Long> otherParticipants = conversation.getParticipantIds().stream()
            .filter(id -> !id.equals(userId))
            .collect(Collectors.toList());

        String conversationName;
        if (conversation.isGroup()) {
            // Nếu là group chat -> lấy tên tất cả participants khác userId
            List<String> participantNames = otherParticipants.stream().limit(3)
                .map(id -> userServiceClient.getUserById(id).getResult().getFirstName())
                .collect(Collectors.toList());
            conversationName = String.join(", ", participantNames);
        } else {
            // Nếu không phải group chat -> chỉ lấy tên của 1 người còn lại
            UserResponse user = userServiceClient.getUserById(otherParticipants.get(0)).getResult();
            conversationName = otherParticipants.isEmpty()
                ? "Người dùng không tồn tại"
                : user.getFirstName() + " " + user.getLastName();
        }

        // Tin nhắn cuối conversation
        Set<Message> messages = conversation.getMessages();
        Message lastMessage = null;
        if (messages != null && !messages.isEmpty()) {
            lastMessage = messages.stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .orElse(null);
        }

        // Lấy danh sách avatarUrls
        String defaultAvatar = "/user_default.avif";
        List<String> avatarUrls = new ArrayList<>();

        if (conversation.getAvatarUrl() != null) {
            avatarUrls.add(conversation.getAvatarUrl());
        } else {
            if (conversation.isGroup()) {
                avatarUrls = otherParticipants.stream()
                    .limit(4)
                    .map(id -> {
                        UserResponse user = userServiceClient.getUserById(id).getResult();
                        return user.getAvatarUrl() != null ? user.getAvatarUrl() : defaultAvatar;
                    })
                    .collect(Collectors.toList());
            } else {
                if (!otherParticipants.isEmpty()) {
                    UserResponse user = userServiceClient.getUserById(otherParticipants.get(0)).getResult();
                    avatarUrls.add(user.getAvatarUrl() != null ? user.getAvatarUrl() : defaultAvatar);
                } else {
                    avatarUrls.add(defaultAvatar);
                }
            }
        }

        return ConversationResponse.builder()
            .id(conversation.getId())
            .name(conversationName)
            .description(conversation.getDescription())
            .avatarUrls(avatarUrls)
            .avatarPublicId(conversation.getAvatarPublicId())
            .color(conversation.getColor())
            .emoji(conversation.getEmoji())
            .lastMessage(messageMapper.toResponse(lastMessage))
            .ownerId(conversation.getOwnerId())
            .participantIds(conversation.getParticipantIds())
            .isGroup(conversation.isGroup())
            .isRead(conversation.isRead())
            .isMuted(conversation.isMuted())
            .isPinned(conversation.isPinned())
            .isArchived(conversation.isArchived())
            .isDeleted(conversation.isDeleted())
            .isBlocked(conversation.isBlocked())
            .isReported(conversation.isReported())
            .isSpam(conversation.isSpam())
            .isMarkedAsUnread(conversation.isMarkedAsUnread())
            .isMarkedAsRead(conversation.isMarkedAsRead())
            .build();
    }
}
