package ChitChat.user_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.user_service.dto.request.UserCreationRequest;
import ChitChat.user_service.dto.request.UserUpdateOtpRequest;
import ChitChat.user_service.dto.request.UserUpdateRequest;
import ChitChat.user_service.dto.response.ApiResponse;
import ChitChat.user_service.dto.response.UserAuthResponse;
import ChitChat.user_service.dto.response.UserDTO;
import ChitChat.user_service.dto.response.UserResponse;
import ChitChat.user_service.mapper.UserMapper;
import ChitChat.user_service.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
        private final UserService userService;
        private final UserMapper userMapper;

        // POST
        @PostMapping("/create")
        public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreationRequest reqUser) {
                var user = this.userService.createUser(reqUser);
                return ApiResponse.<UserResponse>builder()
                                .code(1000)
                                .message("Create user successfully!")
                                .result(userMapper.toUserResponse(user))
                                .build();
        }

        // DELETE
        @DeleteMapping("/delete/{id}")
        public ApiResponse<Void> deleteUserById(@PathVariable Long id) {
                this.userService.deleteUserById(id);
                return ApiResponse.<Void>builder()
                                .code(1000)
                                .message("Delete user with ID " + id + " successfully!")
                                .build();
        }

        // GET
        // Get User by id
        @GetMapping("/get/{id}")
        public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
                var dbUser = this.userService.getUser(id);
                return ApiResponse.<UserResponse>builder()
                                .code(1000)
                                .message("Get user with ID " + id + " successfully!")
                                .result(userMapper.toUserResponse(dbUser))
                                .build();
        }

        // Get User by username or email or phone

        @GetMapping("/get/account")
        public ApiResponse<UserResponse> handleGetAccount(@RequestParam String loginInput) {
                var dbUser = this.userService.handleGetUserByUsernameOrEmailOrPhone(loginInput);
                return ApiResponse.<UserResponse>builder()
                                .code(1000)
                                .message("Get user with login param " + loginInput + " successfully!")
                                .result(userMapper.toUserResponse(dbUser))
                                .build();
        }
        
        @GetMapping("/search")
        public ApiResponse<UserAuthResponse> handleGetUserByLoginInput(@RequestParam String loginInput) {
                var dbUser = this.userService.handleGetUserByLoginInput(loginInput);
                return ApiResponse.<UserAuthResponse>builder()
                                .code(1000)
                                .message("Get user with login param " + loginInput + " successfully!")
                                .result(userMapper.toUserAuthResponse(dbUser))
                                .build();
        }

        @GetMapping("/search&token")
        public ApiResponse<UserAuthResponse> getUserByRefreshTokenAndEmailOrUsernameOrPhone(
                        @RequestParam String refresh_token, 
                        @RequestParam String login) {
                var dbUser = this.userService.getUserByRefreshTokenAndEmailOrUsernameOrPhone(refresh_token, login);
                return ApiResponse.<UserAuthResponse>builder()
                                .code(1000)
                                .message("Get user with login param " + login + " successfully!")
                                .result(userMapper.toUserAuthResponse(dbUser))
                                .build();
        }

        // Get all Users
        @GetMapping("/get/all")
        public ApiResponse<Page<UserDTO>> getAllUsers(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {
                var users = this.userService.getAllUsers(page, size);
                return ApiResponse.<Page<UserDTO>>builder()
                                .code(1000)
                                .message("Get all users successfully!")
                                .result(users.map(userMapper::toUserDTO))
                                .build();
        }

        // Get User's friends
        @GetMapping("/get/friends")
        public ApiResponse<Page<UserDTO>> getUserFriends(
                        @RequestParam Long userId,
                        @RequestParam(defaultValue = "0") int pageNum) {
                var friends = this.userService.getUserFriends(userId, pageNum);
                return ApiResponse.<Page<UserDTO>>builder()
                                .code(1000)
                                .message("Get friends of the user with ID " + userId + " successfully!")
                                .result(friends)
                                .build();
        }

        // Get mutual friends
        @GetMapping("/get/mutualFriends")
        public ApiResponse<Page<UserDTO>> getMutualFriends(
                        @RequestParam Long meId,
                        @RequestParam Long youId,
                        @RequestParam(defaultValue = "0") int pageNum) {
                var friends = this.userService.getMutualFriends(meId, youId, pageNum);
                return ApiResponse.<Page<UserDTO>>builder()
                                .code(1000)
                                .message("Get mutual friends of user with ID " + meId +
                                                " and user with ID " + youId + " successfully!")
                                .result(friends)
                                .build();
        }

        // Get User's friends
        @GetMapping("/get/friends/suggested")
        public ApiResponse<Page<UserDTO>> getSuggestedFriends(
                        @RequestParam Long userId,
                        @RequestParam(defaultValue = "0") int pageNum) {
                var friends = this.userService.getSuggestedFriends(userId, pageNum);
                return ApiResponse.<Page<UserDTO>>builder()
                                .code(1000)
                                .message("Get suggested friends of the user with ID " + userId + " successfully!")
                                .result(friends)
                                .build();
        }

        // PUT
        @PutMapping("/update")
        public ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest reqUser) {
                var user = this.userService.updateUser(reqUser);

                return ApiResponse.<UserResponse>builder()
                                .code(1000)
                                .message("Update user with ID " + reqUser.getId() + " successfully")
                                .result(userMapper.toUserResponse(user))
                                .build();
        }

        @PutMapping("/update/otp")
        public ApiResponse<UserResponse> updateUserOtp(@RequestBody UserUpdateOtpRequest reqUser) {
                var user = this.userService.updateUserOtp(reqUser);

                return ApiResponse.<UserResponse>builder()
                                .code(1000)
                                .message("Update user with ID " + reqUser.getId() + " successfully")
                                .result(userMapper.toUserResponse(user))
                                .build();
        }

        @PutMapping("/update/token")
        public ApiResponse<Void> updateUserToken(
                        @RequestParam String token, 
                        @RequestParam String emailUsernamePhone) {
                this.userService.updateUserToken(token, emailUsernamePhone);

                return ApiResponse.<Void>builder()
                                .code(1000)
                                .message("Update user's token with login value: " + emailUsernamePhone + " successfully")
                                .build();
        }

        @PostMapping("/verify-otp")
        public ApiResponse<Boolean> verifyOtp(@RequestBody Long userId, String otp) {
                var isVerified = this.userService.verifyOtp(userId, otp);
                return ApiResponse.<Boolean>builder()
                                .code(1000)
                                .message("Create user successfully!")
                                .result(isVerified)
                                .build();
        }
}
