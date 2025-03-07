package ChitChat.chat_service.entity;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class ChatMessage { 
    private MessageStatus status;
    private String content; 
    private Long conversationId;
    private Long senderId;
    private Set<Long> receiverId;
    private String url;
    private String createdAt;
}
