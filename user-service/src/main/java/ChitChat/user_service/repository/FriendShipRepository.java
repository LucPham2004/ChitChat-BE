package ChitChat.user_service.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ChitChat.user_service.entity.Friendship;


@Repository
public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, Long> {
    Friendship save(Friendship friendShip);

    void delete(Friendship friendship);

    Optional<Friendship> findById(Long id);

    boolean existsById(Long id);

    @Query("""
           SELECT f FROM  Friendship f 
           WHERE ((f.sender.id = :senderId AND f.recipient.id = :receiverId) OR
                (f.sender.id = :receiverId AND f.recipient.id = :senderId))
           """)
    Friendship findBy2UserIds(Long senderId, Long receiverId);

    @Query("""
            SELECT COUNT(f) FROM Friendship f
            """)
    int countAll();
}
