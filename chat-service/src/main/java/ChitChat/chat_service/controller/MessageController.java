package ChitChat.chat_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/chat")
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class MessageController {

    MessageService service;
    MessageMapper chatMapper;
    
    // Get messages
    @GetMapping("/")
    public ApiResponse<Page<ChatResponse>> getUserMessages(Long senderId, Long recipientId, int pageNum) {
        Page<Message> messages = service.getUserMessages(senderId, recipientId, pageNum);
        return ApiResponse.<Page<ChatResponse>>builder()
            .code(1000)
            .message("Get messages by sender with id: " + senderId + " and recipient with id: " + recipientId + " successfully")
            .result(messages.map(chatMapper::toResponse))
            .build();
    }

    // Delete message
    @DeleteMapping("/{messageId}")
    public ApiResponse<Void> deleteMessage(@PathVariable Long messageId) {
        this.service.deleteMessage(messageId);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Delete message with ID " + messageId + " successfully!")
            .build();
    }
}
