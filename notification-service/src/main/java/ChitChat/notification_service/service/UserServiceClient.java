package ChitChat.notification_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ChitChat.notification_service.dto.UserDTO;
import ChitChat.notification_service.dto.response.ApiResponse;


@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {
	
	@GetMapping("/get/{userId}")
	ApiResponse<UserDTO> getUserById(@PathVariable Long userId);
}

