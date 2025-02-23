package ChitChat.user_service.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ChitChat.user_service.dto.request.UserCreationRequest;
import ChitChat.user_service.dto.request.UserUpdateOtpRequest;
import ChitChat.user_service.dto.request.UserUpdateRequest;
import ChitChat.user_service.dto.response.UserDTO;
import ChitChat.user_service.entity.User;
import ChitChat.user_service.exception.AppException;
import ChitChat.user_service.exception.ErrorCode;
import ChitChat.user_service.mapper.UserMapper;
import ChitChat.user_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    static int USERS_PER_PAGE = 20;

    // GET

    // Get User friends
    public Page<UserDTO> getUserFriends(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> friends = userRepository.findFriends(userId, pageable);

        return getUsersWithMutualFriendsCount(userId, friends);
    }

    // Get User friends
    public Page<UserDTO> getSuggestedFriends(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> friends = userRepository.findSuggestedFriends(userId, pageable);

        return getUsersWithMutualFriendsCount(userId, friends);
    }

    // Get mutual friends
    public Page<UserDTO> getMutualFriends(Long meId, Long youId, int pageNum) {
        if (!userRepository.existsById(meId) || !userRepository.existsById(youId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);

        Page<User> mutualFriends = userRepository.findMutualFriends(meId, youId, pageable);

        return getUsersWithMutualFriendsCount(meId, mutualFriends);
    }

    // Get User by Id
    public Optional<User> findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        return optionalUser;
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }

    // Get all Users
    public Page<User> getAllUsers(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return userRepository.findAll(pageable);
    }

    // POST
    // Create user
    @Transactional
    public User createUser(UserCreationRequest request) {
        log.info("request: " + request.toString() + " existsByUsername: " + userRepository.existsByUsername(request.getUsername()) + " existsByEmail: " + userRepository.existsByEmail(request.getEmail()) + " existsByPhone: " + userRepository.existsByPhone(request.getPhone()));
        if (userRepository.existsByUsername(request.getUsername()) && request.getUsername() != null
                || userRepository.existsByEmail(request.getEmail()) && request.getEmail() != null
                || userRepository.existsByPhone(request.getPhone()) && request.getPhone() != null) {
                    log.info("this username/email/phone is already taken: " + 
                        request.getUsername() + "/" + request.getEmail() + "/" + request.getPhone());
            throw new AppException(ErrorCode.ENTITY_EXISTED);
        }
        User user = userMapper.toUser(request);

        Set<String> authorities = new HashSet<>();
        authorities.add("USER");
        user.setAuthorities(authorities);
        
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    // PUT
    // Edit user info
    @Transactional
    public User updateUser(UserUpdateRequest reqUser) {
        User dbUser = this.findById(reqUser.getId()).get();

        if (reqUser.getFirstName() != null && !reqUser.getFirstName().isEmpty()
                && !reqUser.getFirstName().equals(dbUser.getFirstName())) {
            dbUser.setFirstName(reqUser.getFirstName());
        }

        if (reqUser.getLastName() != null && !reqUser.getLastName().isEmpty()
                && !reqUser.getLastName().equals(dbUser.getLastName())) {
            dbUser.setLastName(reqUser.getLastName());
        }

        if (reqUser.getGender() != null && !reqUser.getGender().equals(dbUser.getGender())) {
            dbUser.setGender(reqUser.getGender());
        }

        if (reqUser.getBio() != null && !reqUser.getBio().isEmpty()
                && !reqUser.getBio().equals(dbUser.getBio())) {
            dbUser.setBio(reqUser.getBio());
        }
        
        if (reqUser.getJob() != null && !reqUser.getJob().isEmpty()
                && !reqUser.getJob().equals(dbUser.getJob())) {
            dbUser.setBio(reqUser.getJob());
        }

        if (reqUser.getDob() != null && !LocalDate.parse(reqUser.getDob()).equals(dbUser.getDob())) {
            dbUser.setDob(LocalDate.parse(reqUser.getDob()));
        }

        if (reqUser.getLocation() != null && !reqUser.getLocation().isEmpty()
                && !reqUser.getLocation().equals(dbUser.getLocation())) {
            dbUser.setLocation(reqUser.getLocation());
        }
        
        if (reqUser.getAvatarUrl() != null && !reqUser.getAvatarUrl().isEmpty()
                && !reqUser.getAvatarUrl().equals(dbUser.getAvatarUrl())) {
            dbUser.setLocation(reqUser.getAvatarUrl());
        }

        return this.userRepository.save(dbUser);
    }

    @Transactional
    public User updateUserOtp(UserUpdateOtpRequest reqUser) {
        User dbUser = this.findById(reqUser.getId()).get();

        if (reqUser.getOtp() != null && !reqUser.getOtp().isEmpty()
                && !reqUser.getOtp().equals(dbUser.getOtp())) {
            dbUser.setOtp(reqUser.getOtp());
        }

        if (reqUser.getOtpGeneratedTime() != null && !reqUser.getOtpGeneratedTime().equals(dbUser.getOtpGeneratedTime())) {
            dbUser.setOtpGeneratedTime(reqUser.getOtpGeneratedTime());
        }

        if (reqUser.isActive() != dbUser.isActive()) {
            dbUser.setActive(reqUser.isActive());
        }
        
        return this.userRepository.save(dbUser);
    }

    // DELETE
    public void deleteUserById(Long id) {
        User dbUser = this.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));

        userRepository.delete(dbUser);
    }

    // Other methods

    // Map to DTO with mutual friends count for many users
    private Page<UserDTO> getUsersWithMutualFriendsCount(Long userId, Page<User> users) {
        List<Long> userIds = users.getContent().stream().map(User::getId).collect(Collectors.toList());
        Map<Long, Long> mutualFriendsCount = userRepository.countMutualFriendsForUsers(userId, userIds);

        return users.map(user -> {
            UserDTO dto = userMapper.toUserDTO(user);
            dto.setMutualFriendsNum(mutualFriendsCount.getOrDefault(user.getId(), 0L));
            return dto;
        });
    }

    public void updateUserToken(String token, String emailUsernamePhone) {
        User currentUser = this.handleGetUserByUsernameOrEmailOrPhone(emailUsernamePhone);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmailOrUsernameOrPhone(String token, String emailUsernamePhone) {
        return this.userRepository.findByRefreshTokenAndEmailOrUsernameOrPhone(token, emailUsernamePhone)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }

    // Get User by username/email/phone
    public User handleGetUserByUsernameOrEmailOrPhone(String loginInput) {
        Optional<User> optionalUser = this.userRepository.findByUsername(loginInput);
        log.info("login input: {}", loginInput);
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByEmail(loginInput);
        }
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByPhone(loginInput);
        }
        if (optionalUser.isEmpty()) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        return optionalUser.get();
    }

    public User handleGetUserByLoginInput(String loginInput) {
        return userRepository.findByUsernameOrEmailOrPhone(loginInput)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }    

    public User getUserByEmail(String email) {
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return null;
        }
        return optionalUser.get();
    }

    public boolean verifyOtp(Long userId, String otp) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(), Instant.now()).getSeconds() < 60) {
            user.setOtp(otp); // Clear OTP after successful verification
            userRepository.save(user);
            return true;
        } else if (!user.getOtp().equals(otp)) {
            throw new AppException(ErrorCode.INVALID_OTP);
        } else {
            throw new AppException(ErrorCode.EXPIRED_OTP);
        }
    }
}
