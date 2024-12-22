package ChitChat.chat_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ChitChat.chat_service.entity.Media;
import ChitChat.chat_service.exception.AppException;
import ChitChat.chat_service.exception.ErrorCode;
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

    static int MEDIAS_PER_PAGE = 10;

    public Media createMedia(String publicId, String url, Long messageId) {
        Media media = new Media();

        media.setUrl(url);
        media.setPublicId(publicId);
        media.setMessage(messageRepository.findById(messageId));

        return mediaRepository.save(media);
    }

    public Media getById(String publicId) {
        return mediaRepository.findById(publicId).get();
    }

    public Page<Media> getBymessageId(Long messageId, int pageNum) {
        if (!messageRepository.existsById(messageId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MEDIAS_PER_PAGE);

        return mediaRepository.findByMessageId(messageId, pageable);
    }
}
