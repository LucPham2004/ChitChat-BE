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
@RequestMapping("/api/message-reaction")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class MessageReactionController {
    MessageReactionService messageReactionService;

    // Create Post messageReaction
    @GetMapping("/post/count/{postId}")
    public ApiResponse<Integer> getMessageReactionCount(@PathVariable Long postId) {
        int messageReactionCount = messageReactionService.getMessageReactionCount(postId);
        return ApiResponse.<Integer>builder()
            .code(1000)
            .message("Create post messageReaction successfully")
            .result(messageReactionCount)
            .build();
    }
    
    // Create Post messageReaction
    @PostMapping("/post/add/{userId}/{postId}")
    public ApiResponse<MessageReaction> createMessageReaction(@PathVariable Long userId, @PathVariable Long postId) {
        MessageReaction messageReaction = messageReactionService.createMessageReaction(userId, postId);
        return ApiResponse.<MessageReaction>builder()
            .code(1000)
            .message("Create post messageReaction successfully")
            .result(messageReaction)
            .build();
    }

    // Delete Post messageReaction
    @DeleteMapping("/post/remove/{userId}/{postId}")
    public ApiResponse<String> deleteMessageReaction(@PathVariable Long userId, @PathVariable Long postId) {
        messageReactionService.deleteMessageReaction(userId, postId);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Delete post messageReaction successfully")
            .result("")
            .build();
    }

}
