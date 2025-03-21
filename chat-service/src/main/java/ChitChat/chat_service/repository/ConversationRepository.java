package ChitChat.chat_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ChitChat.chat_service.entity.Conversation;


@Repository
public interface ConversationRepository extends PagingAndSortingRepository<Conversation, Long> {
    Conversation save(Conversation conversation);

    void delete(Conversation conversation);

    void deleteById(Long id);

    Optional<Conversation> findById(Long id);

    boolean existsById(Long id);

    @Query("""
            SELECT c FROM Conversation c 
            WHERE :userAId MEMBER OF c.participantIds 
            AND :userBId MEMBER OF c.participantIds 
            AND c.isGroup = false
            """)
    Optional<Conversation> findDirectMessage(@Param("userAId") Long userAId, @Param("userBId") Long userBId);
    
    @Query("""
            SELECT c.id FROM Conversation c 
            WHERE :userAId MEMBER OF c.participantIds 
            AND :userBId MEMBER OF c.participantIds 
            AND c.isGroup = false
            """)
    Long findDirectMessageId(@Param("userAId") Long userAId, @Param("userBId") Long userBId);

    @Query("SELECT c FROM Conversation c WHERE :userId MEMBER OF c.participantIds")
    Page<Conversation> findByParticipantIdsContaining(@Param("userId") Long userId, Pageable pageable);
    
    Page<Conversation> findByOwnerId(Long userId, Pageable pageable);

    int countByOwnerId(Long userId);
}
