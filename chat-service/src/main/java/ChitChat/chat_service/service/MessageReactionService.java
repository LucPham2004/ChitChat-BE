package ChitChat.chat_service.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import ChitChat.chat_service.dto.response.UserResponse;
import ChitChat.chat_service.entity.MessageReaction;
import ChitChat.chat_service.exception.AppException;
import ChitChat.chat_service.exception.ErrorCode;
import ChitChat.chat_service.repository.MessageReactionRepository;
import ChitChat.chat_service.repository.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class MessageReactionService {

    MessageReactionRepository messageReactionRepository;
    MessageRepository messageRepository;
    UserServiceClient userServiceClient;

    // Get Message Reaction count
    public int getMessageReactionCount(Long MessageId) {
        return messageReactionRepository.countByMessageId(MessageId);
    }

    // Create Message Reaction
    public MessageReaction createMessageReaction(Long userId, Long MessageId, String emoji) {
        UserResponse user = userServiceClient.getUserById(userId).getResult();
        if(user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        MessageReaction messageReaction = messageReactionRepository.findByUserIdAndMessageId(userId, MessageId);
        if (messageReaction == null) {
            messageReaction = new MessageReaction();
            messageReaction.setEmoji(emoji);
            messageReaction.setUserId(userId);
            messageReaction.setMessage(messageRepository.findById(MessageId).get());
            messageReaction.setCreatedAt(LocalDateTime.now());
            return messageReactionRepository.save(messageReaction);
        } else {
            throw new AppException(ErrorCode.ENTITY_EXISTED);
        }
    }

    // Delete Message Reaction
    public void deleteMessageReaction(Long userId, Long MessageId) {
        UserResponse user = userServiceClient.getUserById(userId).getResult();
        if (!messageRepository.existsById(MessageId) || user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        MessageReaction MessageReaction = messageReactionRepository.findByUserIdAndMessageId(userId, MessageId);
        if (MessageReaction != null) {
            messageReactionRepository.delete(MessageReaction);
        } else {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
    }

}
