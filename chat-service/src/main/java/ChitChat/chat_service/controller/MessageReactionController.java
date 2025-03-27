package ChitChat.chat_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.chat_service.dto.response.ApiResponse;
import ChitChat.chat_service.entity.MessageReaction;
import ChitChat.chat_service.service.MessageReactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/message-reactions")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class MessageReactionController {
    MessageReactionService messageReactionService;

    // Get messageReaction
    @GetMapping("/get/count/message/{messageId}")
    public ApiResponse<Integer> getMessageReactionCount(@PathVariable Long messageId) {
        int messageReactionCount = messageReactionService.getMessageReactionCount(messageId);
        return ApiResponse.<Integer>builder()
            .code(1000)
            .message("get message messageReaction successfully")
            .result(messageReactionCount)
            .build();
    }

    @GetMapping("/get/all/{messageId}")
    public ApiResponse<List<MessageReaction>> getMessageReactions(@PathVariable Long messageId) {
        List<MessageReaction> messageReactions = messageReactionService.getMessageReactions(messageId);
        return ApiResponse.<List<MessageReaction>>builder()
            .code(1000)
            .message("get message reactions successfully")
            .result(messageReactions)
            .build();
    }
    
    // Create messageReaction
    @PostMapping
    public ApiResponse<MessageReaction> createMessageReaction(
            @RequestParam Long userId, 
            @RequestParam Long messageId,
            @RequestParam String emoji) {
        MessageReaction messageReaction = messageReactionService.createMessageReaction(userId, messageId, emoji);
        return ApiResponse.<MessageReaction>builder()
            .code(1000)
            .message("Create message reaction successfully")
            .result(messageReaction)
            .build();
    }

    // Delete messageReaction
    @DeleteMapping("/remove/user/{userId}/message/{messageId}")
    public ApiResponse<String> deleteMessageReaction(@PathVariable Long userId, @PathVariable Long messageId) {
        messageReactionService.deleteMessageReaction(userId, messageId);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Delete message reaction successfully")
            .result("")
            .build();
    }

}
