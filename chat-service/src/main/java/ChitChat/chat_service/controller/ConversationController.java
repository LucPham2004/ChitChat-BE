package ChitChat.chat_service.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/conversations")
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ConversationController {
    
    ConversationService conversationService;
    ConversationMapper conversationMapper;
    
    // GET METHODS

    // not finished
    @GetMapping("/get/joined")
    public ApiResponse<Page<ConversationShortResponse>> getConversationsByParticipantId(
                @RequestParam Long userId, 
                @RequestParam int pageNum) {
        Page<Conversation> conversations = conversationService.getByParticipantId(userId, pageNum);
        return ApiResponse.<Page<ConversationShortResponse>>builder()
            .code(1000)
            .message("Get conversations by participant with id: " + userId + " successfully")
            .result(conversations.map(conversationMapper::toConversationShortResponse))
            .build();
    }

    @GetMapping("/get/owned")
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

    @GetMapping("/get/{id}")
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
    public ApiResponse<ConversationResponse> createConversation(@RequestBody ConversationRequest conversationRequest) {
        Conversation newConversation = conversationService.createConversation(conversationRequest);
        return ApiResponse.<ConversationResponse>builder()
            .code(1000)
            .message("Create conversation successfully")
            .result(conversationMapper.toConversationResponse(newConversation))
            .build();
    }
    
    @PostMapping("/create/many")
    public ApiResponse<List<ConversationResponse>> createConversations(@RequestBody List<ConversationRequest> conversationRequests) {
        List<Conversation> newConversations = conversationService.createManyConversations(conversationRequests);

        return ApiResponse.<List<ConversationResponse>>builder()
            .code(1000)
            .message("Create conversations successfully")
            .result(newConversations.stream()
                .map(conversationMapper::toConversationResponse)
                .toList())
            .build();
    }

    
    // PUT METHODS

    @PutMapping("/update")
    public ApiResponse<ConversationResponse> updateConversation(@RequestBody ConversationRequest conversationRequest) {
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
