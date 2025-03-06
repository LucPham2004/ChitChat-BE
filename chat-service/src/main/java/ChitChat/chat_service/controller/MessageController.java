package ChitChat.chat_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.chat_service.dto.response.ApiResponse;
import ChitChat.chat_service.dto.response.ChatResponse;
import ChitChat.chat_service.entity.Message;
import ChitChat.chat_service.mapper.MessageMapper;
import ChitChat.chat_service.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class MessageController {

    MessageService service;
    MessageMapper chatMapper;
    
    // Get messages

    @GetMapping("/get/conversation")
    public ApiResponse<Page<ChatResponse>> getConversationMessages(
                @RequestParam Long conversationId,
                @RequestParam int pageNum) {
        Page<Message> messages = service.getConversationMessages(conversationId, pageNum);
        return ApiResponse.<Page<ChatResponse>>builder()
            .code(1000)
            .message("Get messages by conversation with id: " + conversationId + " successfully")
            .result(messages.map(chatMapper::toResponse))
            .build();
    }

    @GetMapping("/get")
    public ApiResponse<Page<ChatResponse>> getUserMessages(
                @RequestParam Long senderId,
                @RequestParam int pageNum) {
        Page<Message> messages = service.getUserMessages(senderId, pageNum);
        return ApiResponse.<Page<ChatResponse>>builder()
            .code(1000)
            .message("Get messages by sender with id: " + senderId + " successfully")
            .result(messages.map(chatMapper::toResponse))
            .build();
    }

    // Send message
    // @PutMapping("/send")
    // public ApiResponse<ChatResponse> sendMessage(
    //             @RequestParam Long senderId,
    //             @RequestParam Long receiverId,
    //             @RequestParam String content) {
    //     Message message = service.sendMessage(senderId, receiverId, content);
    //     return ApiResponse.<ChatResponse>builder()
    //         .code(1000)
    //         .message("Send message successfully!")
    //         .result(chatMapper.toResponse(message))
    //         .build();
    // }

    // Delete message
    @DeleteMapping("/delete/{messageId}")
    public ApiResponse<Void> deleteMessage(@PathVariable Long messageId) {
        this.service.deleteMessage(messageId);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Delete message with ID " + messageId + " successfully!")
            .build();
    }

    // Update message
    @PutMapping("/update")
    public ApiResponse<Void> updateMessage(
                @RequestParam Long messageId,
                @RequestParam String content) {
        this.service.updateMessage(messageId, content);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Update message with ID " + messageId + " successfully!")
            .build();
    }

}
