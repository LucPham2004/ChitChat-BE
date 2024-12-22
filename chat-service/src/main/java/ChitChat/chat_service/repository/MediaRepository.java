package ChitChat.chat_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ChitChat.chat_service.entity.Media;

@Repository
public interface MediaRepository extends PagingAndSortingRepository<Media, String> {
    Media save(Media media);

    void delete(Media media);

    Optional<Media> findById(String id);

    boolean existsById(String id);

    Page<Media> findByPostId(Long postId, Pageable pageable);

    int countByPostId(Long id);
}
