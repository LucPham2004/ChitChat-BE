package ChitChat.chat_service.dto.response;

import java.time.LocalDateTime;
import java.util.List;
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
public class ConversationShortResponse {
    
    Long id;
    String name;
    String lastMessage;
    boolean isThisYourLastMessage;
    LocalDateTime lastMessageTime;
    List<String> avatarUrls;
    String avatarPublicId;

    Long ownerId;
    Set<Long> participantIds;
    boolean isGroup;
    boolean isRead;
    
}
