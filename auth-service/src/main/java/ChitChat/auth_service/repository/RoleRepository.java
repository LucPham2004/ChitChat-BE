package ChitChat.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ChitChat.auth_service.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByAuthority(String authority);
    Optional<Role> findById(String id);
    
    @Query("""
            SELECT COUNT(ro) FROM Role ro
            """)
    int countAll();
}
