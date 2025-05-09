package ChitChat.chat_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ChitChat.chat_service.entity.MessageReaction;


@Repository
public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
    MessageReaction findByUserIdAndMessageId(Long userId, Long messageId);

    List<MessageReaction> findByMessageId(Long messageId);

    int countByMessageId(Long messageId);

    int countByUserId(Long id);
    
    @Query("""
            SELECT COUNT(mr) FROM MessageReaction mr
            """)
    int countAll();
}
