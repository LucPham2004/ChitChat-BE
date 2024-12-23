package ChitChat.chat_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
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

    int countBySenderIdAndRecipientId(Long senderId, Long recipientId);

    @Query("""
            SELECT COUNT(m) FROM Message m
            """)
    int countAll();
}
