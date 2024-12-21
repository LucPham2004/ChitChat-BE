package ChitChat.user_service.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ChitChat.user_service.entity.User;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
     User save(User user);

     void delete(User user);

     void deleteById(Long id);

     boolean existsById(Long id);

     boolean existsByUsername(String username);

     boolean existsByEmail(String email);

     boolean existsByPhone(String phone);

     Optional<User> findById(long id);

     Optional<User> findByUsername(String username);

     Optional<User> findByEmail(String username);

     Optional<User> findByPhone(String phoneNumber);

     @Query("""
               SELECT COUNT(u) FROM User u
               """)
     int countAll();

     // Find User's friends
     @Query("""
               SELECT u FROM User u
               JOIN Friendship f
               ON (f.requester = u OR f.requestReceiver = u)
               WHERE (f.requester.id = :userId OR f.requestReceiver.id = :userId)
               AND f.status = 'ACCEPTED'
               AND u.id != :userId
               """)
     Page<User> findFriends(@Param("userId") Long userId, Pageable pageable);

     @Query("""
               SELECT COUNT(u) FROM User u
               JOIN Friendship f
               ON (f.requester = u OR f.requestReceiver = u)
               WHERE (f.requester.id = :userId OR f.requestReceiver.id = :userId)
               AND f.status = 'ACCEPTED'
               AND u.id != :userId
               """)
     int countFriends(@Param("userId") Long userId);

	// Friend suggestion by finding other users having most mutual friends
     @Query("""
		SELECT u FROM User u
		WHERE u.id <> :userId
		AND u.id NOT IN (
			SELECT f.requestReceiver.id FROM Friendship f WHERE f.requester.id = :userId
			UNION
			SELECT f2.requester.id FROM Friendship f2 WHERE f2.requestReceiver.id = :userId
		)
		AND u.id IN (
			SELECT f3.requestReceiver.id FROM Friendship f3 
			WHERE f3.requester.id IN (
				SELECT f4.requestReceiver.id FROM Friendship f4 WHERE f4.requester.id = :userId
				UNION
				SELECT f5.requester.id FROM Friendship f5 WHERE f5.requestReceiver.id = :userId
			)
			UNION
			SELECT f6.requester.id FROM Friendship f6 
			WHERE f6.requestReceiver.id IN (
				SELECT f7.requestReceiver.id FROM Friendship f7 WHERE f7.requester.id = :userId
				UNION
				SELECT f8.requester.id FROM Friendship f8 WHERE f8.requestReceiver.id = :userId
			)
		)
		GROUP BY u.id
		ORDER BY COUNT(u) DESC, FUNCTION('RAND')
		""")
     Page<User> findSuggestedFriends(@Param("userId") Long userId, Pageable pageable);

     // 2 User's mutual friends
     @Query("""
               SELECT u FROM User u WHERE u.id IN
               (SELECT f.requestReceiver.id FROM Friendship f
               WHERE f.requester.id = :userAId AND f.requestReceiver.id IN
               (SELECT f2.requestReceiver.id FROM Friendship f2 WHERE f2.requester.id = :userBId))
               OR u.id IN
               (SELECT f3.requester.id FROM Friendship f3
               WHERE f3.requestReceiver.id = :userAId AND f3.requester.id IN
               (SELECT f4.requester.id FROM Friendship f4 WHERE f4.requestReceiver.id = :userBId))
               """)
     Page<User> findMutualFriends(@Param("userAId") Long userAId, @Param("userBId") Long userBId, Pageable pageable);

     @Query("""
               SELECT COUNT(f) FROM Friendship f
               WHERE (f.requester.id = :userAId AND f.requestReceiver.id IN
               (SELECT f2.requestReceiver.id FROM Friendship f2 WHERE f2.requester.id = :userBId))
               OR (f.requestReceiver.id = :userAId AND f.requester.id IN
               (SELECT f3.requester.id FROM Friendship f3 WHERE f3.requestReceiver.id = :userBId))
               """)
     int countMutualFriends(@Param("userAId") Long userAId, @Param("userBId") Long userBId);

     @Query("""
               SELECT u.id, COUNT(f) FROM Friendship f
               JOIN User u ON (f.requester.id = :userId AND f.requestReceiver.id IN :memberIds
               OR f.requestReceiver.id = :userId AND f.requester.id IN :memberIds)
               GROUP BY u.id
               """)
     Map<Long, Long> countMutualFriendsForUsers(@Param("userId") Long userId,
               @Param("memberIds") List<Long> memberIds);

     @Query(value = "SELECT * FROM users u WHERE u.refresh_token = :token AND (u.email = :emailUsernamePhone OR u.username = :emailUsernamePhone OR u.phone = :emailUsernamePhone)", nativeQuery = true)
     Optional<User> findByRefreshTokenAndEmailOrUsernameOrPhone(@Param("token") String token, 
               @Param("emailUsernamePhone") String emailUsernamePhone);
}
