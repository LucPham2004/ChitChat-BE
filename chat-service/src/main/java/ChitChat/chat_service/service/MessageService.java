package ChitChat.chat_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import ChitChat.chat_service.dto.UserMessageDTO;
import ChitChat.chat_service.dto.request.ChatRequest;
import ChitChat.chat_service.entity.Message;
import ChitChat.chat_service.exception.AppException;
import ChitChat.chat_service.exception.ErrorCode;
import ChitChat.chat_service.mapper.MessageMapper;
import ChitChat.chat_service.repository.ConversationRepository;
import ChitChat.chat_service.repository.MessageRepository;
import ChitChat.chat_service.utils.KafkaConstants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class MessageService {

    MessageRepository messageRepository;
    MessageMapper messageMapper;
    ConversationRepository conversationRepository;
    UserServiceClient userServiceClient;
    KafkaTemplate<String, Message> kafkaTemplate;

    static int MESSAGES_PER_PAGE = 20;

    // Get messages

    public Message getMessage(Long messageId) {
        if(!messageRepository.existsById(messageId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        return messageRepository.findById(messageId).get();
    }

    public Page<Message> getConversationMessages(Long conversationId, int pageNum) {
        if(!conversationRepository.existsById(conversationId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MESSAGES_PER_PAGE);

        return messageRepository.findByConversationId(conversationId, pageable);
    }

    public Page<Message> getUserMessages(Long senderId, int pageNum) {
        UserMessageDTO user = userServiceClient.getUserById(senderId).getResult();
        if(user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MESSAGES_PER_PAGE);

        return messageRepository.findBySenderId(senderId, pageable);
    }

    // Send Message
    public void sendMessage(ChatRequest chatRequest) {
        Message message = messageRepository.save(messageMapper.toMessage(chatRequest)); 

        kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, 
                           String.valueOf(chatRequest.getConversationId()), 
                           message);
    }

    // Delete Message
    public void deleteMessage(Long messageId) {
        if(!messageRepository.existsById(messageId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        messageRepository.delete(messageRepository.findById(messageId).get());
    }

    // Update Message
    public Message updateMessage(Long messageId, String content) {
        if(!messageRepository.existsById(messageId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Message message = messageRepository.findById(messageId).get();
        message.setContent(content);
        return messageRepository.save(message);
    }
}
