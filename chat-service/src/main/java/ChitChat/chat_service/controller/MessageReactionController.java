package ChitChat.chat_service.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.chat_service.dto.response.ApiResponse;
import ChitChat.chat_service.entity.MessageReaction;
import ChitChat.chat_service.service.MessageReactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/message-reactions")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class MessageReactionController {
    MessageReactionService messageReactionService;

    // Create messageReaction
    @GetMapping("/count/message/{messageId}")
    public ApiResponse<Integer> getMessageReactionCount(@PathVariable Long messageId) {
        int messageReactionCount = messageReactionService.getMessageReactionCount(messageId);
        return ApiResponse.<Integer>builder()
            .code(1000)
            .message("Create message messageReaction successfully")
            .result(messageReactionCount)
            .build();
    }
    
    // Create messageReaction
    @PostMapping("/add/user/{userId}/message/{messageId}")
    public ApiResponse<MessageReaction> createMessageReaction(@PathVariable Long userId, @PathVariable Long messageId) {
        MessageReaction messageReaction = messageReactionService.createMessageReaction(userId, messageId);
        return ApiResponse.<MessageReaction>builder()
            .code(1000)
            .message("Create message messageReaction successfully")
            .result(messageReaction)
            .build();
    }

    // Delete messageReaction
    @DeleteMapping("/remove/user/{userId}/message/{messageId}")
    public ApiResponse<String> deleteMessageReaction(@PathVariable Long userId, @PathVariable Long messageId) {
        messageReactionService.deleteMessageReaction(userId, messageId);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Delete message messageReaction successfully")
            .result("")
            .build();
    }

}
