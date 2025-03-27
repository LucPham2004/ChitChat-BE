package ChitChat.chat_service.service;

import java.time.LocalDateTime;
import java.util.List;

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
    public int getMessageReactionCount(Long messageId) {
        return messageReactionRepository.countByMessageId(messageId);
    }

    public List<MessageReaction> getMessageReactions(Long messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        return messageReactionRepository.findByMessageId(messageId);
    }

    // Create Message Reaction
    public MessageReaction createMessageReaction(Long userId, Long messageId, String emoji) {
        UserResponse user = userServiceClient.getUserById(userId).getResult();
        if(user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        MessageReaction messageReaction = messageReactionRepository.findByUserIdAndMessageId(userId, messageId);
        if (messageReaction == null) {
            messageReaction = new MessageReaction();
            messageReaction.setEmoji(emoji);
            messageReaction.setUserId(userId);
            messageReaction.setMessage(messageRepository.findById(messageId).get());
            messageReaction.setCreatedAt(LocalDateTime.now());
            return messageReactionRepository.save(messageReaction);
        } else {
            return messageReaction;
        }
    }

    // Delete Message Reaction
    public void deleteMessageReaction(Long userId, Long messageId) {
        UserResponse user = userServiceClient.getUserById(userId).getResult();
        if (!messageRepository.existsById(messageId) || user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        MessageReaction MessageReaction = messageReactionRepository.findByUserIdAndMessageId(userId, messageId);
        if (MessageReaction != null) {
            messageReactionRepository.delete(MessageReaction);
        } else {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
    }

}
