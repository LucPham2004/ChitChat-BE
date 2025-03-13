package ChitChat.chat_service.mapper;

import java.time.LocalDateTime;
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
    UserServiceClient userServiceClient;
    
    public Conversation toConversation(ConversationRequest conversationRequest) {
        return Conversation.builder()
            .name(conversationRequest.getName() != null ?
                conversationRequest.getName() : null)
            .description(conversationRequest.getDescription())
            .avatarUrl(conversationRequest.getAvatarUrl() != null && !conversationRequest.isGroup() ? 
                conversationRequest.getAvatarUrl() : "/user_default.avif")
            .avatarPublicId(conversationRequest.getAvatarPublicId())
            .color(conversationRequest.getColor())
            .emoji(conversationRequest.getEmoji())
            .participantIds(conversationRequest.getParticipantIds())
            .ownerId(conversationRequest.getOwnerId())
            .lastMessage(conversationRequest.getLastMessage())
            .lastMessageTime(conversationRequest.getLastMessageTime() != null ?
                LocalDateTime.parse(conversationRequest.getLastMessageTime()) : null
                )
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
        Set<Message> message = conversation.getMessages();
        Message lastMessage = new Message();
        if(message != null) {
            if(message.size() > 0) {
                lastMessage = message.stream()
                    .max((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                    .orElse(null);
            }
        }

        // Danh sách participant ngoại trừ userId
        List<Long> otherParticipants = conversation.getParticipantIds().stream()
        .filter(id -> !id.equals(userId))
        .collect(Collectors.toList());

        String conversationName;
        if (conversation.isGroup()) {
            // Nếu là group chat -> lấy tên tất cả participants khác userId
            List<String> participantNames = otherParticipants.stream()
                .map(id -> userServiceClient.getUserById(id).getResult().getFirstName())
                .collect(Collectors.toList());
            conversationName = String.join(", ", participantNames);
        } else {
            // Nếu không phải group chat -> chỉ lấy tên của 1 người còn lại
            UserResponse user = userServiceClient.getUserById(otherParticipants.get(0)).getResult();
            conversationName = otherParticipants.isEmpty()
                ? "Người dùng không tồn tại" // Trường hợp lỗi
                : user.getFirstName() + " " + user.getLastName();
        }

        return ConversationShortResponse.builder()
            .id(conversation.getId())
            .name(conversationName)
            .lastMessage(lastMessage != null ? lastMessage.getContent() : null)
            .lastMessageTime(lastMessage != null ? lastMessage.getCreatedAt() : null)
            .avatarUrl(conversation.getAvatarUrl() != null && !conversation.isGroup() ? 
                conversation.getAvatarUrl() : "/user_default.avif")
            .avatarPublicId(conversation.getAvatarPublicId())
            .ownerId(conversation.getOwnerId())
            .participantIds(conversation.getParticipantIds())
            .isGroup(conversation.isGroup())
            .isRead(conversation.isRead())
            .build();
    }

    public ConversationResponse toConversationResponse(Conversation conversation, Long userId) {
        Set<Message> message = conversation.getMessages();
        Message lastMessage = new Message();
        if(message != null) {
            if(message.size() > 0) {
                lastMessage = message.stream()
                    .max((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                    .orElse(null);
            }
        }
        // Danh sách participant ngoại trừ userId
        List<Long> otherParticipants = conversation.getParticipantIds().stream()
        .filter(id -> !id.equals(userId))
        .collect(Collectors.toList());

        String conversationName;
        if (conversation.isGroup()) {
            // Nếu là group chat -> lấy tên tất cả participants khác userId
            List<String> participantNames = otherParticipants.stream()
                .map(id -> userServiceClient.getUserById(id).getResult().getFirstName())
                .collect(Collectors.toList());
            conversationName = String.join(", ", participantNames);
        } else {
            // Nếu không phải group chat -> chỉ lấy tên của 1 người còn lại
            UserResponse user = userServiceClient.getUserById(otherParticipants.get(0)).getResult();
            conversationName = otherParticipants.isEmpty()
                ? "Người dùng không tồn tại" // Trường hợp lỗi
                : user.getFirstName() + " " + user.getLastName();
        }

        return ConversationResponse.builder()
            .id(conversation.getId())
            .name(conversationName)
            .description(conversation.getDescription())
            .avatarUrl(conversation.getAvatarUrl() != null && !conversation.isGroup() ? 
                conversation.getAvatarUrl() : "/user_default.avif")
            .avatarPublicId(conversation.getAvatarPublicId())
            .color(conversation.getColor())
            .emoji(conversation.getEmoji())
            .lastMessage(lastMessage != null ? lastMessage.getContent() : null)
            .lastMessageTime(lastMessage != null ? lastMessage.getCreatedAt() : null)
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
