package ChitChat.auth_service.dto.request;

import java.time.Instant;

import lombok.Data;

@Data
public class UserUpdateOtpRequest {
    private Long id;

    private String otp;

    private Instant otpGeneratedTime;

    private boolean isActive;
}
