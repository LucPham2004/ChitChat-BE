package ChitChat.chat_service.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ChitChat.chat_service.dto.request.ChatRequest;
import ChitChat.chat_service.dto.response.UserResponse;
import ChitChat.chat_service.entity.Conversation;
import ChitChat.chat_service.entity.Media;
import ChitChat.chat_service.entity.Message;
import ChitChat.chat_service.exception.AppException;
import ChitChat.chat_service.exception.ErrorCode;
import ChitChat.chat_service.mapper.MessageMapper;
import ChitChat.chat_service.repository.ConversationRepository;
import ChitChat.chat_service.repository.MediaRepository;
import ChitChat.chat_service.repository.MessageRepository;
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
    MediaRepository mediaRepository;
    SimpMessagingTemplate template;

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
        // Get nearest messages by conversation
        Pageable pageable = PageRequest.of(pageNum, MESSAGES_PER_PAGE, Sort.by(Sort.Direction.DESC, "createdAt"));

        return messageRepository.findByConversationId(conversationId, pageable);
    }

    public Page<Message> getUserMessages(Long senderId, int pageNum) {
        UserResponse user = userServiceClient.getUserById(senderId).getResult();
        if(user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MESSAGES_PER_PAGE);

        return messageRepository.findBySenderId(senderId, pageable);
    }

    // Search messages by keyword in a conversation
    public Page<Message> findMessagesByKeyword(Long conversationId, String keyword, int pageNum) {
        if(!conversationRepository.existsById(conversationId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MESSAGES_PER_PAGE);

        return messageRepository.findByConversationIdAndContentContainingIgnoreCase(conversationId, keyword, pageable);
    }

    // Send Message
    public void sendMessage(ChatRequest chatRequest) {
        Conversation conversation = conversationRepository.findById(chatRequest.getConversationId()).get();
        if(conversation == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Message message = messageMapper.toMessage(chatRequest);
        messageRepository.save(message);

        if (chatRequest.getPublicIds() != null && chatRequest.getUrls() != null &&
            chatRequest.getHeights() != null && chatRequest.getWidths() != null &&
            chatRequest.getResourceTypes() != null && chatRequest.getFileNames() != null) {

            if (chatRequest.getPublicIds().length != chatRequest.getUrls().length
                || chatRequest.getPublicIds().length != chatRequest.getFileNames().length
                || chatRequest.getPublicIds().length != chatRequest.getHeights().length
                || chatRequest.getPublicIds().length != chatRequest.getWidths().length
                || chatRequest.getPublicIds().length != chatRequest.getResourceTypes().length) {
                throw new IllegalArgumentException("The size of publicIds and urls/heights/widths/types must be the same.");
            }
        
            Set<Media> medias = new HashSet<>();
        
            for (int i = 0; i < chatRequest.getPublicIds().length; i++) {
                Media media = new Media();
                media.setPublicId(chatRequest.getPublicIds()[i]);
                media.setUrl(chatRequest.getUrls()[i]);
                media.setFileName(chatRequest.getFileNames()[i]);
                media.setHeight(chatRequest.getHeights()[i]);
                media.setWidth(chatRequest.getWidths()[i]);
                media.setResourceType(chatRequest.getResourceTypes()[i]);
                
                media.setMessage(message);
                media.setConversation(conversation);
        
                medias.add(mediaRepository.save(media));
            }
        
            message.setMedias(medias);
            conversation.setMedias(medias);
            messageRepository.save(message);
            conversationRepository.save(conversation);
        }

        chatRequest.setId(message.getId());
        
        template.convertAndSend("/topic/conversation/" + chatRequest.getConversationId(), chatRequest);
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
