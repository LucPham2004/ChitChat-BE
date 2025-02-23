package ChitChat.chat_service.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ChitChat.chat_service.dto.request.ConversationRequest;
import ChitChat.chat_service.entity.Conversation;
import ChitChat.chat_service.exception.AppException;
import ChitChat.chat_service.exception.ErrorCode;
import ChitChat.chat_service.mapper.ConversationMapper;
import ChitChat.chat_service.repository.ConversationRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ConversationService {
    
    ConversationRepository conversationRepository;
    //MessageRepository messageRepository;
    ConversationMapper conversationMapper;
    
    static int CONVERSATIONS_PER_PAGE = 20;

    // GET METHODS

    public Conversation getById(Long id) {
        return conversationRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }

    // public Page<Conversation> getByParticipantIds(Long userId, int pageNum) {
    //     Pageable pageable = PageRequest.of(pageNum, CONVERSATIONS_PER_PAGE);

    //     return conversationRepository.findByParticipantIds(userId, pageable);
    // }

    public Page<Conversation> getByOwnerId(Long userId, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, CONVERSATIONS_PER_PAGE);

        return conversationRepository.findByOwnerId(userId, pageable);
    }

    // POST METHODS

    public Conversation createConversation(ConversationRequest conversationRequest) {
        return conversationRepository.save(conversationMapper.toConversation(conversationRequest));
    }

    public List<Conversation> createManyConversations(List<ConversationRequest> conversationRequests) {
        List<Conversation> newConversations = conversationRequests.stream()
            .map(conversationMapper::toConversation)
            .map(conversationRepository::save)
            .toList();
        return newConversations;
    }

    // PUT METHODS

    @Transactional
    public Conversation updateConversation(ConversationRequest conversationRequest) {
        Conversation conversation = conversationRepository.findById(conversationRequest.getId()).get();

        if(conversationRequest.getName() != null && !conversationRequest.getName().isEmpty()
            && !conversationRequest.getName().equals(conversation.getName())) {
            conversation.setName(conversationRequest.getName());
        }

        if(conversationRequest.getDescription() != null && !conversationRequest.getDescription().isEmpty()
            && !conversationRequest.getDescription().equals(conversation.getDescription())) {
            conversation.setDescription(conversationRequest.getDescription());
        }

        if(conversationRequest.getColor() != null && !conversationRequest.getColor().isEmpty()
            && !conversationRequest.getColor().equals(conversation.getColor())) {
            conversation.setColor(conversationRequest.getColor());
        }

        if(conversationRequest.getEmoji() != null && !conversationRequest.getEmoji().isEmpty()
            && !conversationRequest.getEmoji().equals(conversation.getEmoji())) {
            conversation.setEmoji(conversationRequest.getEmoji());
        }

        if(conversationRequest.getParticipantIds() != null && !conversationRequest.getParticipantIds().isEmpty()
            && !conversationRequest.getParticipantIds().equals(conversation.getParticipantIds())) {
            conversation.setParticipantIds(conversationRequest.getParticipantIds());
        }

        if(conversationRequest.getOwnerId() != null
            && !conversationRequest.getOwnerId().equals(conversation.getOwnerId())) {
            conversation.setOwnerId(conversationRequest.getOwnerId());
        }

        if(conversationRequest.getLastMessage() != null && !conversationRequest.getLastMessage().isEmpty()
            && !conversationRequest.getLastMessage().equals(conversation.getLastMessage())) {
            conversation.setLastMessage(conversationRequest.getLastMessage());
        }

        if(conversationRequest.isGroup() != conversation.isGroup()) {
            conversation.setGroup(conversationRequest.isGroup());
        }

        if(conversationRequest.isRead() != conversation.isRead()) {
            conversation.setRead(conversationRequest.isRead());
        }

        if(conversationRequest.isMuted() != conversation.isMuted()) {
            conversation.setMuted(conversationRequest.isMuted());
        }

        if(conversationRequest.isPinned() != conversation.isPinned()) {
            conversation.setPinned(conversationRequest.isPinned());
        }

        if(conversationRequest.isArchived() != conversation.isArchived()) {
            conversation.setArchived(conversationRequest.isArchived());
        }

        if(conversationRequest.isDeleted() != conversation.isDeleted()) {
            conversation.setDeleted(conversationRequest.isDeleted());
        }

        if(conversationRequest.isBlocked() != conversation.isBlocked()) {
            conversation.setBlocked(conversationRequest.isBlocked());
        }

        if(conversationRequest.isReported() != conversation.isReported()) {
            conversation.setReported(conversationRequest.isReported());
        }

        if(conversationRequest.isSpam() != conversation.isSpam()) {
            conversation.setSpam(conversationRequest.isSpam());
        }

        if(conversationRequest.isMarkedAsUnread() != conversation.isMarkedAsUnread()) {
            conversation.setMarkedAsUnread(conversationRequest.isMarkedAsUnread());
        }

        if(conversationRequest.isMarkedAsRead() != conversation.isMarkedAsRead()) {
            conversation.setMarkedAsRead(conversationRequest.isMarkedAsRead());
        }

        return conversationRepository.save(conversation);
    }

    // DELETE METHODS

    public void deleteConversation(Conversation conversation) {
        conversationRepository.delete(conversation);
    }

    public void deleteConversationById(Long id) {
        conversationRepository.deleteById(id);
    }

    // OTHER METHODS
    public boolean existsById(Long id) {
        return conversationRepository.existsById(id);
    }

    public int countByOwnerId(Long userId) {
        return conversationRepository.countByOwnerId(userId);
    }
}
