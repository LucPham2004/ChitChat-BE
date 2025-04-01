package ChitChat.chat_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ChitChat.chat_service.entity.Media;
import ChitChat.chat_service.exception.AppException;
import ChitChat.chat_service.exception.ErrorCode;
import ChitChat.chat_service.repository.ConversationRepository;
import ChitChat.chat_service.repository.MediaRepository;
import ChitChat.chat_service.repository.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaService {

    MediaRepository mediaRepository;
    MessageRepository messageRepository;
    ConversationRepository conversationRepository;

    static int MEDIAS_PER_PAGE = 20;

    public Media createMedia(String publicId, String url, Long messageId) {
        Media media = new Media();

        media.setUrl(url);
        media.setPublicId(publicId);
        media.setMessage(messageRepository.findById(messageId).get());

        return mediaRepository.save(media);
    }

    public Media getById(String publicId) {
        return mediaRepository.findById(publicId).get();
    }

    public Page<Media> getMediaByMessageId(Long messageId, int pageNum) {
        if (!messageRepository.existsById(messageId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MEDIAS_PER_PAGE, Sort.by(Sort.Direction.DESC, "createdAt"));

        return mediaRepository.findByMessageId(messageId, pageable);
    }
    
    public Page<Media> getMediasAndFilesByConversationId(Long conversationId, int pageNum) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MEDIAS_PER_PAGE, Sort.by(Sort.Direction.DESC, "createdAt"));

        return mediaRepository.findByConversationId(conversationId, pageable);
    }
    
    public Page<Media> getMediasByConversationId(Long conversationId, int pageNum) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MEDIAS_PER_PAGE, Sort.by(Sort.Direction.DESC, "createdAt"));
        String type = "image";
        return mediaRepository.findByConversationIdAndResourceType(conversationId, type, pageable);
    }
    
    public Page<Media> getRawFilesByConversationId(Long conversationId, int pageNum) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MEDIAS_PER_PAGE, Sort.by(Sort.Direction.DESC, "createdAt"));
        String type = "raw";
        return mediaRepository.findByConversationIdAndResourceType(conversationId, type, pageable);
    }
}
