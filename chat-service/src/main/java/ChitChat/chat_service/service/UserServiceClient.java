package ChitChat.chat_service.service;

import org.springframework.cloud.openfeign.FeignClient;

import ChitChat.chat_service.dto.UserMessageDTO;
import ChitChat.chat_service.dto.response.ApiResponse;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {
	
	ApiResponse<UserMessageDTO> getUserById(Long userId);
}

