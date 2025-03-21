package ChitChat.user_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ChitChat.user_service.dto.response.ApiResponse;


//http://gateway:8888/chat-service/conversations
//http://localhost:8082/conversations
@FeignClient(name = "chat-service", url = "http://gateway:8888/chat-service/conversations")
public interface ConversationServiceClient {

    @GetMapping("/get/id")
    ApiResponse<Long> getDirectMessageId(@RequestParam Long selfId, @RequestParam Long otherId);
}

