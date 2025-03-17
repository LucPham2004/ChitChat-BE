package ChitChat.chat_service.dto.response;

import java.time.LocalDateTime;
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
public class ChatResponse {
    Long id;
    Long conversationId;
    Long senderId;
    Set<Long> recipientId;
    
    String content;
    String[] publicIds;
    String[] urls;

    Boolean isRead;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
