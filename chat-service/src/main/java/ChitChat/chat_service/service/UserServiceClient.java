package ChitChat.chat_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ChitChat.chat_service.dto.response.ApiResponse;
import ChitChat.chat_service.dto.response.UserResponse;

//http://gateway:8888/user-service/users
//http://localhost:8081/users
@FeignClient(name = "user-service", url = "http://gateway:8888/user-service/users")
public interface UserServiceClient {
	
	@GetMapping("/get/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable Long id);
}

