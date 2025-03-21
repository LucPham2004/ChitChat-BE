package ChitChat.chat_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.chat_service.dto.request.ConversationRequest;
import ChitChat.chat_service.dto.response.ApiResponse;
import ChitChat.chat_service.dto.response.ChatParticipants;
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
            .result(conversations.map(conv -> conversationMapper.toConversationShortResponse(conv, userId)))
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
            .result(conversations.map(conv -> conversationMapper.toConversationShortResponse(conv, userId)))
            .build();
    }
    
    @GetMapping("/get/{selfId}/{otherId}")
    public ApiResponse<ConversationResponse> getDirectMessage(@PathVariable Long selfId, @PathVariable Long otherId) {
        Conversation conversation = conversationService.getDirectMessage(selfId, otherId);
        return ApiResponse.<ConversationResponse>builder()
            .code(1000)
            .message("Get conversation successfully")
            .result(conversationMapper.toConversationResponse(conversation, selfId))
            .build();
    }

    @GetMapping("/get/{convId}/{userId}")
    public ApiResponse<ConversationResponse> getConversationById(@PathVariable Long convId, @PathVariable Long userId) {
        Conversation conversation = conversationService.getById(convId);
        return ApiResponse.<ConversationResponse>builder()
            .code(1000)
            .message("Get conversation by id: " + convId + " successfully")
            .result(conversationMapper.toConversationResponse(conversation, userId))
            .build();
    }

    @GetMapping("/get/participants/{convId}")
    public ApiResponse<List<ChatParticipants>> getParticipantsByConvId(@PathVariable Long convId) {
        List<ChatParticipants> participants = conversationService.getParticipantsByConvId(convId);
        return ApiResponse.<List<ChatParticipants>>builder()
            .code(1000)
            .message("Get conversation participants with id: " + convId + " successfully")
            .result(participants)
            .build();
    }

    // POST METHODS

    @PostMapping("/create")
    public ApiResponse<ConversationResponse> createConversation(@RequestBody ConversationRequest conversationRequest) {
        Conversation newConversation = conversationService.createConversation(conversationRequest);
        return ApiResponse.<ConversationResponse>builder()
            .code(1000)
            .message("Create conversation successfully")
            .result(conversationMapper.toConversationResponse(newConversation, conversationRequest.getOwnerId()))
            .build();
    }
    
    @PostMapping("/create/many")
    public ApiResponse<List<ConversationResponse>> createConversations(@RequestBody List<ConversationRequest> conversationRequests) {
        List<Conversation> newConversations = conversationService.createManyConversations(conversationRequests);

        return ApiResponse.<List<ConversationResponse>>builder()
            .code(1000)
            .message("Create conversations successfully")
            .result(newConversations.stream()
                .map(conversation -> conversationMapper.toConversationResponse(conversation, conversationRequests.get(0).getOwnerId()))
                .toList())
            .build();
    }

    
    // PUT METHODS

    @PutMapping("/update/{userId}")
    public ApiResponse<ConversationResponse> updateConversation(
                @RequestBody ConversationRequest conversationRequest,
                @PathVariable Long userId) {
        Conversation updatedConversation = conversationService.updateConversation(conversationRequest);
        return ApiResponse.<ConversationResponse>builder()
            .code(1000)
            .message("Update conversation successfully")
            .result(conversationMapper.toConversationResponse(updatedConversation, userId))
            .build();
    }

    @PatchMapping("/update/partially/{id}/{userId}")
    public ApiResponse<ConversationResponse> updateConversationPartially(
                @PathVariable Long id, 
                @PathVariable Long userId,
                @RequestBody Map<String, Object> updates) {
        Conversation updatedConversation = conversationService.updateConversationPartially(id, updates);
        return ApiResponse.<ConversationResponse>builder()
            .code(1000)
            .message("Update conversation successfully")
            .result(conversationMapper.toConversationResponse(updatedConversation, userId))
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

    @GetMapping("/get/id")
    public ApiResponse<Long> getDirectMessageId(@RequestParam Long selfId, @RequestParam Long otherId) {
        return ApiResponse.<Long>builder()
            .code(1000)
            .message("Get conversation successfully")
            .result(conversationService.getDirectMessageId(selfId, otherId))
            .build();
    }

}
