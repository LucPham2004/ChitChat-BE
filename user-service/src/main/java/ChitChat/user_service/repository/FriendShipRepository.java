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
           WHERE ((f.requester.id = :requesterId AND f.requestReceiver.id = :receiverId) OR
                (f.requester.id = :receiverId AND f.requestReceiver.id = :requesterId))
           """)
    Friendship findBy2UserIds(Long requesterId, Long receiverId);

    @Query("""
            SELECT COUNT(f) FROM Friendship f
            """)
    int countAll();
}
