package ChitChat.chat_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.chat_service.dto.request.ConversationRequest;
import ChitChat.chat_service.dto.response.ApiResponse;
import ChitChat.chat_service.dto.response.ConversationResponse;
import ChitChat.chat_service.dto.response.ConversationShortResponse;
import ChitChat.chat_service.entity.Conversation;
import ChitChat.chat_service.mapper.ConversationMapper;
import ChitChat.chat_service.service.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conversations")
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ConversationController {
    
    ConversationService conversationService;
    ConversationMapper conversationMapper;
    
    // GET METHODS

    @GetMapping("/participants")
    public ApiResponse<Page<ConversationShortResponse>> getConversationsByParticipantIds(
                @RequestParam Long userId, 
                @RequestParam int pageNum) {
        Page<Conversation> conversations = conversationService.getByParticipantIds(userId, pageNum);
        return ApiResponse.<Page<ConversationShortResponse>>builder()
            .code(1000)
            .message("Get conversations by participant with id: " + userId + " successfully")
            .result(conversations.map(conversationMapper::toConversationShortResponse))
            .build();
    }

    @GetMapping("/owner")
    public ApiResponse<Page<ConversationShortResponse>> getConversationsByOwnerId(
                @RequestParam Long userId, 
                @RequestParam int pageNum) {
        Page<Conversation> conversations = conversationService.getByOwnerId(userId, pageNum);
        return ApiResponse.<Page<ConversationShortResponse>>builder()
            .code(1000)
            .message("Get conversations by owner with id: " + userId + " successfully")
            .result(conversations.map(conversationMapper::toConversationShortResponse))
            .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ConversationResponse> getConversationById(@PathVariable Long id) {
        Conversation conversation = conversationService.getById(id);
        return ApiResponse.<ConversationResponse>builder()
            .code(1000)
            .message("Get conversation by id: " + id + " successfully")
            .result(conversationMapper.toConversationResponse(conversation))
            .build();
    }

    // POST METHODS

    @PostMapping("/create")
    public ApiResponse<ConversationResponse> createConversation(ConversationRequest conversationRequest) {
        Conversation newConversation = conversationService.createConversation(conversationRequest);
        return ApiResponse.<ConversationResponse>builder()
            .code(1000)
            .message("Create conversation successfully")
            .result(conversationMapper.toConversationResponse(newConversation))
            .build();
    }
    
    // PUT METHODS

    @PutMapping("/update")
    public ApiResponse<ConversationResponse> updateConversation(ConversationRequest conversationRequest) {
        Conversation updatedConversation = conversationService.updateConversation(conversationRequest);
        return ApiResponse.<ConversationResponse>builder()
            .code(1000)
            .message("Update conversation successfully")
            .result(conversationMapper.toConversationResponse(updatedConversation))
            .build();
    }

    // DELETE METHODS

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteConversation(@PathVariable Long id) {
        Conversation conversation = conversationService.getById(id);
        conversationService.deleteConversation(conversation);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Delete conversation with ID " + id + " successfully!")
            .build();
    }

}
