package ChitChat.chat_service.listener;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import ChitChat.chat_service.dto.request.ChatRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;
    
    public void listen(ChatRequest message) {
        log.info("sending via listener..");

        Set<Long> receiverIds = message.getRecipientId();
        
        
        for (Long receiverId : receiverIds) {
            template.convertAndSend("/topic/" + receiverId, message);
        }
        
    }
}
