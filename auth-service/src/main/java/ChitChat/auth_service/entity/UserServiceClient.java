package ChitChat.auth_service.entity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ChitChat.auth_service.dto.response.UserResponse;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {
    @GetMapping("/search")
    UserResponse findUserByUsernameOrEmailOrPhone(@RequestParam String login);
}

