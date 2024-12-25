package ChitChat.chat_service.dto.request;

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
public class ConversationRequest {

    Long id;
    String name;
    String description;
    String color;
    String emoji;

    Set<Long> participantIds;
    Long ownerId;

    String lastMessage;

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
