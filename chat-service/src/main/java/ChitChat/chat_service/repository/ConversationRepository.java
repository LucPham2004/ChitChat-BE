package ChitChat.chat_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ChitChat.chat_service.entity.Conversation;


@Repository
public interface ConversationRepository extends PagingAndSortingRepository<Conversation, Long> {
    Conversation save(Conversation conversation);

    void delete(Conversation conversation);

    void deleteById(Long id);

    Optional<Conversation> findById(Long id);

    boolean existsById(Long id);

    Page<Conversation> findByParticipantIds(Long userId, Pageable pageable);
    
    Page<Conversation> findByOwnerId(Long userId, Pageable pageable);

    int countByParticipantIds(Long userId);

    int countByOwnerId(Long userId);
}
