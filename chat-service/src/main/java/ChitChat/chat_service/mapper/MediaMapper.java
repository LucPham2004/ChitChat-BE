package ChitChat.chat_service.mapper;

import org.springframework.stereotype.Component;

import ChitChat.chat_service.dto.response.MediaResponse;
import ChitChat.chat_service.entity.Media;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class MediaMapper {
    

    public MediaResponse toResponse(Media media) {
        MediaResponse response = new MediaResponse();

        response.setPublicId(media.getPublicId());
        response.setUrl(media.getUrl());
        response.setConversationId(media.getConversation().getId());
        response.setMessageId(media.getMessage().getId());
        response.setCreatedAt(media.getCreatedAt());
        response.setUpdatedAt(media.getUpdatedAt());

        return response;
    }
}
