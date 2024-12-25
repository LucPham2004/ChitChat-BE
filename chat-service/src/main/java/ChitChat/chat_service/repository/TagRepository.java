package ChitChat.chat_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ChitChat.chat_service.entity.Tag;


@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Override
    Page<Tag> findAll(Pageable pageable);

    @Query("""
            SELECT t from Tag t
            ORDER BY COUNT(t.message) DESC
            """)
    Page<Tag> findAllOrderByMessageDesc(Pageable pageable);
    
    @Query("""
        SELECT COUNT(t) FROM Tag t
        """)
    int countAll();
}
