package ChitChat.user_service.mapper;

import org.springframework.stereotype.Component;

import ChitChat.user_service.dto.request.UserCreationRequest;
import ChitChat.user_service.dto.response.UserAuthResponse;
import ChitChat.user_service.dto.response.UserDTO;
import ChitChat.user_service.dto.response.UserResponse;
import ChitChat.user_service.entity.User;
import ChitChat.user_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper {

    UserRepository userRepository;

    public User toUser(UserCreationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setGender(request.getGender());

        return user;
    };

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setPhone(user.getPhone());
        userResponse.setAvatarUrl(user.getAvatarUrl());
        userResponse.setDob(user.getDob());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        userResponse.setGender(user.getGender());
        userResponse.setFriendNum(userRepository.countFriends(user.getId()));

        return userResponse;
    }

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLocation(user.getLocation());
        userDTO.setAvatarUrl(user.getAvatarUrl());
        userDTO.setFriendNum(userRepository.countFriends(user.getId()));

        return userDTO;
    }

    public UserAuthResponse toUserAuthResponse(User user) {
        UserAuthResponse userResponse = new UserAuthResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setPassword(user.getPassword());

        return userResponse;
    }

}
