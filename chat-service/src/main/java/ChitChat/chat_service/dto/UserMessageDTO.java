package ChitChat.chat_service.dto;

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
public class UserMessageDTO {
    Long userId;
    
    Set<Long> conversationIds;
    Set<Long> messageIds;
    Set<Long> messageReactionIds;
}
