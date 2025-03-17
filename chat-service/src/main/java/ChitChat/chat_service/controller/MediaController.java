package ChitChat.chat_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.chat_service.dto.response.ApiResponse;
import ChitChat.chat_service.dto.response.MediaResponse;
import ChitChat.chat_service.entity.Media;
import ChitChat.chat_service.mapper.MediaMapper;
import ChitChat.chat_service.service.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medias")
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class MediaController {
    
    MediaService mediaService;
    MediaMapper mediaMapper;

    @GetMapping("/get/{id}")
    public ApiResponse<MediaResponse> getMediaById(@PathVariable String id) {
       Media media = mediaService.getById(id);
        return ApiResponse.<MediaResponse>builder()
            .code(1000)
            .message("Get media with id: " + id + " successfully")
            .result(mediaMapper.toResponse(media))
            .build();
    }

    @GetMapping("/get/message")
    public ApiResponse<Page<MediaResponse>> getMediaByMessageId(
                @RequestParam Long messageId, 
                @RequestParam int pageNum) {
        Page<Media> medias = mediaService.getMediaByMessageId(messageId, pageNum);
        return ApiResponse.<Page<MediaResponse>>builder()
            .code(1000)
            .message("Get medias by message with id: " + messageId + " successfully")
            .result(medias.map(media -> mediaMapper.toResponse(media)))
            .build();
    }

    
    @GetMapping("/get/conversation")
    public ApiResponse<Page<MediaResponse>> getMediaByConversationId(
                @RequestParam Long conversationId, 
                @RequestParam int pageNum) {
        Page<Media> medias = mediaService.getMediaByConversationId(conversationId, pageNum);
        return ApiResponse.<Page<MediaResponse>>builder()
            .code(1000)
            .message("Get medias by message with id: " + conversationId + " successfully")
            .result(medias.map(media -> mediaMapper.toResponse(media)))
            .build();
    }

}
