package ChitChat.chat_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ChitChat.chat_service.entity.Conversation;
import ChitChat.chat_service.repository.ConversationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ConversationService {
    
    ConversationRepository conversationRepository;
    
    static int CONVERSATIONS_PER_PAGE = 20;

    // GET METHODS

    public Conversation getById(Long id) {
        return conversationRepository.findById(id).get();
    }

    public Page<Conversation> getByParticipantIds(Long userId, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, CONVERSATIONS_PER_PAGE);

        return conversationRepository.findByParticipantIds(userId, pageable);
    }

    public Page<Conversation> getByOwnerId(Long userId, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, CONVERSATIONS_PER_PAGE);

        return conversationRepository.findByOwnerId(userId, pageable);
    }

    // POST METHODS

    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    // PUT METHODS

    public Conversation updateConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    // DELETE METHODS

    public void deleteConversation(Conversation conversation) {
        conversationRepository.delete(conversation);
    }

    // OTHER METHODS
    public boolean existsById(Long id) {
        return conversationRepository.existsById(id);
    }

    public int countByParticipantIds(Long userId) {
        return conversationRepository.countByParticipantIds(userId);
    }

    public int countByOwnerId(Long userId) {
        return conversationRepository.countByOwnerId(userId);
    }
}
