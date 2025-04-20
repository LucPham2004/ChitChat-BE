package ChitChat.chat_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ChitChat.chat_service.entity.Media;

@Repository
public interface MediaRepository extends PagingAndSortingRepository<Media, String> {
    Media save(Media media);

    void delete(Media media);

    Optional<Media> findById(String id);

    boolean existsById(String id);

    Page<Media> findByMessageId(Long messageId, Pageable pageable);
    
    Page<Media> findByConversationId(Long conversationId, Pageable pageable);

    Page<Media> findByConversationIdAndResourceType(Long conversationId, String type, Pageable pageable);
    
    Page<Media> findByConversationIdAndResourceTypeNot(Long conversationId, String type, Pageable pageable);

    int countByMessageId(Long id);
}
