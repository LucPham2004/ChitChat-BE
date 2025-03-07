package ChitChat.chat_service.listener;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import ChitChat.chat_service.entity.Message;
import ChitChat.chat_service.utils.KafkaConstants;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID,
            containerFactory = "kafkaListenerContainerFactory"
    )
    
    public void listen(Message message) {
        log.info("sending via kafka listener..");

        Set<Long> receiverIds = message.getReceiverIds();
        
        if (receiverIds.size() == 1) {
            Long receiverId = receiverIds.iterator().next();
            template.convertAndSend("/topic/private/" + receiverId, message);
        } else {
            for (Long receiverId : receiverIds) {
                template.convertAndSend("/topic/group/" + receiverId, message);
            }
        }
    }
}
