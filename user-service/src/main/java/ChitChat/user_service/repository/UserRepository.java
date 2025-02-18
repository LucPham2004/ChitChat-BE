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
               ON (f.sender = u OR f.recipient = u)
               WHERE (f.sender.id = :userId OR f.recipient.id = :userId)
               AND f.status = 'Accepted'
               AND u.id != :userId
               """)
     Page<User> findFriends(@Param("userId") Long userId, Pageable pageable);

     @Query("""
               SELECT COUNT(u) FROM User u
               JOIN Friendship f
               ON (f.sender = u OR f.recipient = u)
               WHERE (f.sender.id = :userId OR f.recipient.id = :userId)
               AND f.status = 'Accepted'
               AND u.id != :userId
               """)
     int countFriends(@Param("userId") Long userId);

	// Friend suggestion by finding other users having most mutual friends
     @Query("""
		SELECT u FROM User u
		WHERE u.id <> :userId
		AND u.id NOT IN (
			SELECT f.recipient.id FROM Friendship f WHERE f.sender.id = :userId
			UNION
			SELECT f2.sender.id FROM Friendship f2 WHERE f2.recipient.id = :userId
		)
		AND u.id IN (
			SELECT f3.recipient.id FROM Friendship f3 
			WHERE f3.sender.id IN (
				SELECT f4.recipient.id FROM Friendship f4 WHERE f4.sender.id = :userId
				UNION
				SELECT f5.sender.id FROM Friendship f5 WHERE f5.recipient.id = :userId
			)
			UNION
			SELECT f6.sender.id FROM Friendship f6 
			WHERE f6.recipient.id IN (
				SELECT f7.recipient.id FROM Friendship f7 WHERE f7.sender.id = :userId
				UNION
				SELECT f8.sender.id FROM Friendship f8 WHERE f8.recipient.id = :userId
			)
		)
		GROUP BY u.id
		ORDER BY COUNT(u) DESC, FUNCTION('RAND')
		""")
     Page<User> findSuggestedFriends(@Param("userId") Long userId, Pageable pageable);

     // 2 User's mutual friends
     @Query("""
               SELECT u FROM User u WHERE u.id IN
               (SELECT f.recipient.id FROM Friendship f
               WHERE f.sender.id = :userAId AND f.recipient.id IN
               (SELECT f2.recipient.id FROM Friendship f2 WHERE f2.sender.id = :userBId))
               OR u.id IN
               (SELECT f3.sender.id FROM Friendship f3
               WHERE f3.recipient.id = :userAId AND f3.sender.id IN
               (SELECT f4.sender.id FROM Friendship f4 WHERE f4.recipient.id = :userBId))
               """)
     Page<User> findMutualFriends(@Param("userAId") Long userAId, @Param("userBId") Long userBId, Pageable pageable);

     @Query("""
               SELECT COUNT(f) FROM Friendship f
               WHERE (f.sender.id = :userAId AND f.recipient.id IN
               (SELECT f2.recipient.id FROM Friendship f2 WHERE f2.sender.id = :userBId))
               OR (f.recipient.id = :userAId AND f.sender.id IN
               (SELECT f3.sender.id FROM Friendship f3 WHERE f3.recipient.id = :userBId))
               """)
     int countMutualFriends(@Param("userAId") Long userAId, @Param("userBId") Long userBId);

     @Query("""
               SELECT u.id, COUNT(f) FROM Friendship f
               JOIN User u ON (f.sender.id = :userId AND f.recipient.id IN :memberIds
               OR f.recipient.id = :userId AND f.sender.id IN :memberIds)
               GROUP BY u.id
               """)
     Map<Long, Long> countMutualFriendsForUsers(@Param("userId") Long userId,
               @Param("memberIds") List<Long> memberIds);

     @Query("SELECT u FROM User u WHERE u.username = :loginInput OR u.email = :loginInput OR u.phone = :loginInput")
     Optional<User> findByUsernameOrEmailOrPhone(@Param("loginInput") String loginInput);
               
     @Query(value = "SELECT * FROM users u WHERE u.refresh_token = :token AND (u.email = :emailUsernamePhone OR u.username = :emailUsernamePhone OR u.phone = :emailUsernamePhone)", nativeQuery = true)
     Optional<User> findByRefreshTokenAndEmailOrUsernameOrPhone(@Param("token") String token, 
               @Param("emailUsernamePhone") String emailUsernamePhone);
}
