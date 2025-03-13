package ChitChat.chat_service.mapper;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Component;

import ChitChat.chat_service.dto.request.ConversationRequest;
import ChitChat.chat_service.dto.response.ConversationResponse;
import ChitChat.chat_service.dto.response.ConversationShortResponse;
import ChitChat.chat_service.entity.Conversation;
import ChitChat.chat_service.entity.Message;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class ConversationMapper {

    //MessageRepository messageRepository;
    
    public Conversation toConversation(ConversationRequest conversationRequest) {
        return Conversation.builder()
            .name(conversationRequest.getName())
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

    public ConversationShortResponse toConversationShortResponse(Conversation conversation) {
        Set<Message> message = conversation.getMessages();
        Message lastMessage = new Message();
        if(message != null) {
            if(message.size() > 0) {
                lastMessage = message.stream()
                    .max((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                    .orElse(null);
            }
        }

        return ConversationShortResponse.builder()
            .id(conversation.getId())
            .name(conversation.getName())
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

    public ConversationResponse toConversationResponse(Conversation conversation) {
        Set<Message> message = conversation.getMessages();
        Message lastMessage = new Message();
        if(message != null) {
            if(message.size() > 0) {
                lastMessage = message.stream()
                    .max((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                    .orElse(null);
            }
        }

        return ConversationResponse.builder()
            .id(conversation.getId())
            .name(conversation.getName())
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
