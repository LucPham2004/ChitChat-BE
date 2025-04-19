package ChitChat.user_service.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.user_service.dto.response.ApiResponse;
import ChitChat.user_service.dto.response.FriendShipResponse;
import ChitChat.user_service.enums.FriendshipStatus;
import ChitChat.user_service.mapper.FriendshipMapper;
import ChitChat.user_service.service.FriendshipService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/friendships")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendshipController {

    FriendshipService friendshipService;
    FriendshipMapper mapper;

    @GetMapping("/get/status")
    public ApiResponse<FriendShipResponse> getFriendStatus(
					@RequestParam Long senderId, 
					@RequestParam Long recipientId) {
            var response = this.friendshipService.getFriendStatus(senderId, recipientId);
            if(response == null) {
                return ApiResponse.<FriendShipResponse>builder()
                            .code(1000)
                            .message("2 Users are not Friends")
                            .build();
            }
            return ApiResponse.<FriendShipResponse>builder()
                            .code(1000)
                            .message("Get friendship status from user with ID: " + senderId + 
                                        " and user with ID: " + recipientId + " successfully!")
                            .result(mapper.toFriendShipResponse(response))
                            .build();
    }

    @PostMapping("/request")
    public ApiResponse<FriendShipResponse> sendFriendRequest(
					@RequestParam Long senderId, 
					@RequestParam Long recipientId) {
            var response = this.friendshipService.sendFriendRequest(senderId, recipientId);
            return ApiResponse.<FriendShipResponse>builder()
                            .code(1000)
                            .message("Send friend request from user with ID: " + senderId + 
                                        " to user with ID: " + recipientId + " successfully!")
                            .result(mapper.toFriendShipResponse(response))
                            .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteFriendShip(
                    @RequestParam Long senderId, 
                    @RequestParam Long recipientId) { 
            this.friendshipService.deleteFriendShip(senderId, recipientId);
            return ApiResponse.<Void>builder()
                            .code(1000)
                            .message("Delete friendship successfully!")
                            .build();
    }

    @PutMapping("/update")
    public ApiResponse<FriendShipResponse> editFriendShipStatus(
					@RequestParam Long senderId, 
					@RequestParam Long recipientId, 
					@RequestParam FriendshipStatus status) {
        	var response = this.friendshipService.editFriendShipStatus(senderId, recipientId, status);
            return ApiResponse.<FriendShipResponse>builder()
                            .code(1000)
                            .message("Edit friendship status successfully!")
                            .result(mapper.toFriendShipResponse(response))
                            .build();
    }

}
