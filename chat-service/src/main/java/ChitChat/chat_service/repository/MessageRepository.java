package ChitChat.chat_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ChitChat.chat_service.entity.Message;


@Repository
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
    Message save(Message Message);

    void delete(Message Message);

    Optional<Message> findById(Long id);

    boolean existsById(Long id);

    Page<Message> findByConversationId(Long conversationId, Pageable pageable);

    Page<Message> findBySenderId(Long senderId, Pageable pageable);

    // Search messages by keyword in a conversation
    @Query(value = "SELECT * FROM messages WHERE conversation_id = :conversationId AND LOWER(content) LIKE LOWER(CONCAT('%', :contentKeyword, '%'))", 
        nativeQuery = true)
    Page<Message> findByConversationIdAndContentContainingIgnoreCase(@Param("conversationId") Long conversationId, @Param("contentKeyword") String contentKeyword, Pageable pageable);

    int countBySenderId(Long senderId);

    @Query("""
            SELECT COUNT(m) FROM Message m
            """)
    int countAll();
}
