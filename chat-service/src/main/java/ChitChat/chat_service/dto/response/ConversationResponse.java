package ChitChat.chat_service.dto.response;

import java.util.Set;

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
public class ConversationResponse {
    
    Long id;
    String name;
    String description;
    String color;
    String emoji;
    String lastMessage;
    String avatarUrl;
    String avatarPublicId;

    Long ownerId;
    Set<Long> participantIds;
    
    boolean isGroup;
    boolean isRead;
    boolean isMuted;
    boolean isPinned;
    boolean isArchived;
    boolean isDeleted;
    boolean isBlocked;
    boolean isReported;
    boolean isSpam;
    boolean isMarkedAsUnread;
    boolean isMarkedAsRead;
}
