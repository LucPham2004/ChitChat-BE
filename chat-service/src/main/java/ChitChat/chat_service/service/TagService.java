package ChitChat.chat_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ChitChat.chat_service.entity.Tag;
import ChitChat.chat_service.exception.AppException;
import ChitChat.chat_service.exception.ErrorCode;
import ChitChat.chat_service.repository.TagRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class TagService {
    TagRepository tagRepository;

    static int TAGS_PER_PAGE = 20;

    public Page<Tag> getAllTags(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, TAGS_PER_PAGE);
        return tagRepository.findAll(pageable);
    }

    public Page<Tag> getAllTagsSortedByMessageCount(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, TAGS_PER_PAGE);
        return tagRepository.findAllOrderByMessageDesc(pageable);
    }
    
    public Tag createTag(Long userId) {
        Tag tag = new Tag();
        tag.setUserId(userId);
        return tagRepository.save(tag);
    }

    public void deleteTag(Long id) {
        if(tagRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        tagRepository.delete(tagRepository.findById(id).get());
    }
}
