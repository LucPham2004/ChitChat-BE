package ChitChat.chat_service.dto.request;

import java.util.List;
import java.util.Set;

import ChitChat.chat_service.entity.MessageReaction;
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
public class ChatRequest {
    Long id;
    Long conversationId;
    Long senderId;
    Set<Long> recipientId;
    
    String content;
    
    List<MessageReaction> reactions;

    String[] publicIds;
    String[] urls;
    String[] fileNames;
    Long[] heights;
    Long[] widths;
    String[] resourceTypes;
}
