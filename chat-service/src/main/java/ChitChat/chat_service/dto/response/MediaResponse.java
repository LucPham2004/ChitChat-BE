package ChitChat.chat_service.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class MediaResponse {
    String publicId;
    String url;
    Long height;
    Long width;
    String resourceType;

    Long messageId;
    Long conversationId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
