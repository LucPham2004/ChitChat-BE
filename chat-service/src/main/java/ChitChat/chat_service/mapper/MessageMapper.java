package ChitChat.chat_service.mapper;

import org.springframework.stereotype.Component;

import ChitChat.chat_service.dto.request.ChatRequest;
import ChitChat.chat_service.dto.response.ChatResponse;
import ChitChat.chat_service.entity.ChatMessage;
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
        message.setSenderId(userServiceClient.getUserById(request.getSenderId()).getResult().getUserId());
        message.setConversation(conversationRepository.findById(request.getConversationId()).get());
        message.setRead(false);

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
        response.setContent(message.getContent());
        response.setConversationId(message.getConversation().getId());
        response.setSenderId(message.getSenderId());
        response.setIsRead(false);
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());

        return response;
    }
}
