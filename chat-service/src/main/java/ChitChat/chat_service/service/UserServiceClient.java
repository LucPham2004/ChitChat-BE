package ChitChat.chat_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ChitChat.chat_service.dto.UserMessageDTO;
import ChitChat.chat_service.dto.response.ApiResponse;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {
	
	@GetMapping("/get/{userId}")
	ApiResponse<UserMessageDTO> getUserById(@PathVariable Long userId);
}

