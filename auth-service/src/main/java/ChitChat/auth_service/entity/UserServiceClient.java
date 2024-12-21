package ChitChat.auth_service.entity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ChitChat.auth_service.dto.request.UserCreationRequest;
import ChitChat.auth_service.dto.request.UserUpdateOtpRequest;
import ChitChat.auth_service.dto.response.ApiResponse;
import ChitChat.auth_service.dto.response.UserAuthResponse;
import ChitChat.auth_service.dto.response.UserResponse;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

    // Get Methods
    @GetMapping("/search")
    ApiResponse<UserAuthResponse> handleGetUserByUsernameOrEmailOrPhone(
            @RequestParam String loginInput);

    @GetMapping("/seach&token")
    ApiResponse<UserAuthResponse> getUserByRefreshTokenAndEmailOrUsernameOrPhone(
            @RequestParam String refresh_token, 
            @RequestParam String login);

    // Post Methods
    @PostMapping("/create")
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest reqUser);

    @PostMapping("/verify-otp")
    ApiResponse<Boolean> verifyOtp(@RequestBody Long userId, String otp);

    // Put
    @PutMapping("/update/otp")
    ApiResponse<UserResponse> updateUserOtp(@RequestBody UserUpdateOtpRequest reqUser);
    
    @PutMapping("/update/token")
    UserAuthResponse updateUserToken(
            @RequestParam String token, 
            @RequestParam String emailUsernamePhone);
}

