package ChitChat.chat_service.mapper;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import ChitChat.chat_service.dto.request.ChatRequest;
import ChitChat.chat_service.dto.response.ChatResponse;
import ChitChat.chat_service.entity.ChatMessage;
import ChitChat.chat_service.entity.Media;
import ChitChat.chat_service.entity.Message;
import ChitChat.chat_service.entity.MessageStatus;
import ChitChat.chat_service.repository.ConversationRepository;
import ChitChat.chat_service.service.UserServiceClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class MessageMapper {
    ConversationRepository conversationRepository;
    UserServiceClient userServiceClient;
    
    public Message toMessage(ChatRequest request) {
        Message message = new Message();
        message.setContent(request.getContent());

        message.setSenderId(userServiceClient.getUserById(request.getSenderId()).getResult().getId());
        message.setReceiverIds(request.getRecipientId());
        message.setConversation(conversationRepository.findById(request.getConversationId()).get());
        message.setReactions(new HashSet<>());
        message.setTags(new HashSet<>());
        
        message.setRead(false);
        message.setStatus(MessageStatus.DELIVERED);

        return message;
    }

    public Message toMessage(ChatMessage request) {
        Message message = new Message();
        message.setContent(request.getContent());
        message.setUrl(request.getUrl());
        message.setConversation(conversationRepository.findById(request.getConversationId()).get());
        message.setSenderId(request.getSenderId());
        message.setReceiverIds(request.getReceiverId());
        message.setStatus(MessageStatus.DELIVERED);
        message.setRead(false);

        return message;
    }

    public ChatResponse toResponse(Message message) {
        ChatResponse response = new ChatResponse();
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setConversationId(message.getConversation().getId());
        response.setSenderId(message.getSenderId());
        response.setRecipientId(message.getReceiverIds());
        response.setIsRead(false);
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());

        Set<Media> medias = message.getMedias();
        if (medias != null && !medias.isEmpty()) {
            String[] publicIds = medias.stream()
                .map(Media::getPublicId)
                .toArray(String[]::new);

            String[] urls = medias.stream()
                .map(Media::getUrl)
                .toArray(String[]::new);
            
            String[] fileNames = medias.stream()
                .map(Media::getFileName)
                .toArray(String[]::new);

            Long[] heights = medias.stream()
                .map(Media::getHeight)
                .toArray(Long[]::new);
            
            Long[] widths = medias.stream()
                .map(Media::getWidth)
                .toArray(Long[]::new);
            
            String[] resourceTypes = medias.stream()
                .map(Media::getResourceType)
                .toArray(String[]::new);

            response.setPublicIds(publicIds);
            response.setUrls(urls);
            response.setFileNames(fileNames);
            response.setHeights(heights);
            response.setWidths(widths);
            response.setResourceTypes(resourceTypes);
        }

        return response;
    }
}
