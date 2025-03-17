package ChitChat.chat_service.dto.response;

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
public class ChatParticipants {
    Long id;
    String username;
    String avatarPublicId;
    String avatarUrl;
    String firstName;
    String lastName;
}
