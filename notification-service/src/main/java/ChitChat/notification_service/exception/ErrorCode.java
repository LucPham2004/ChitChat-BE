package ChitChat.notification_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),

    ENTITY_EXISTED(1002, "Entity existed!", HttpStatus.BAD_REQUEST),

    ENTITY_NOT_EXISTED(1005, "Entity not existed", HttpStatus.NOT_FOUND),

    NO_REFRESH_TOKEN(1010, "You don't have refresh token in cookies", HttpStatus.BAD_REQUEST),

    INVALID_ACCESS_TOKEN(1011, "Your access token is not valid", HttpStatus.BAD_REQUEST), 

    ;

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}
