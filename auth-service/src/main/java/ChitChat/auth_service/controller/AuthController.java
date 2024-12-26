package ChitChat.auth_service.controller;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ChitChat.auth_service.dto.request.LoginRequest;
import ChitChat.auth_service.dto.request.UserCreationRequest;
import ChitChat.auth_service.dto.request.UserUpdateOtpRequest;
import ChitChat.auth_service.dto.response.ApiResponse;
import ChitChat.auth_service.dto.response.LoginResponse;
import ChitChat.auth_service.dto.response.UserAuthResponse;
import ChitChat.auth_service.dto.response.UserResponse;
import ChitChat.auth_service.entity.Role;
import ChitChat.auth_service.entity.UserServiceClient;
import ChitChat.auth_service.exception.AppException;
import ChitChat.auth_service.exception.ErrorCode;
import ChitChat.auth_service.repository.RoleRepository;
import ChitChat.auth_service.utils.EmailUtil;
import ChitChat.auth_service.utils.OtpUtil;
import ChitChat.auth_service.utils.SecurityUtils;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    AuthenticationManagerBuilder authenticationManagerBuilder;
    UserServiceClient userServiceClient;
    RoleRepository roleRepository; 
    SecurityUtils securityUtils;
    EmailUtil emailUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());

        // Authenticate the user
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("authentication principal: {}", authentication.getPrincipal());

        // Prepare the login response
        LoginResponse loginResponse = new LoginResponse();
        UserAuthResponse currentUserDB = userServiceClient.handleGetUserByUsernameOrEmailOrPhone(loginRequest.getUsername())
                .getResult();

        Set<Role> authorities = currentUserDB.getAuthorityIds().stream()
				.map(roleRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toSet());

        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                currentUserDB.getId(),
                currentUserDB.getEmail(),
                currentUserDB.getUsername(),
                authorities);
        loginResponse.setUser(userLogin);

        // Generate tokens
        String access_token = securityUtils.createAccessToken(authentication.getName(), loginResponse);
        loginResponse.setAccess_token(access_token);

        String refresh_token = securityUtils.createRefreshToken(loginRequest.getUsername(), loginResponse);

        // Update refresh token for the user in the database
        userServiceClient.updateUserToken(refresh_token, loginRequest.getUsername());

        // Set refresh token as an HTTP-only cookie
        ResponseCookie resCookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(securityUtils.refreshTokenExpiration)
                .build();

        // Create the ApiResponse
        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                .code(1000)
                .message("Login successfully")
                .result(loginResponse)
                .build();

        // Build the response with the body and headers
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString()) // Set the cookie in the response header
                .body(apiResponse); // Return the body with the API response
    }

    @GetMapping("/account")
    public ApiResponse<LoginResponse.UserGetAccount> getAccount() {
        String login = SecurityUtils.getCurrentUserLogin().isPresent()
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";

        UserResponse currentUserDB = userServiceClient.handleGetAccount(login).getResult();

        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();
        LoginResponse.UserGetAccount userGetAccount = new LoginResponse.UserGetAccount();

        Set<Role> authorities = currentUserDB.getAuthorityIds().stream()
				.map(roleRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toSet());

        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setUsername(currentUserDB.getUsername());
            userLogin.setAuthorities(authorities);

            userGetAccount.setUser(userLogin);
        }

        return ApiResponse.<LoginResponse.UserGetAccount>builder()
                .code(1000)
                .message("Get current user successfully!")
                .result(userGetAccount)
                .build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "blabla") String refresh_token, String message) {
        if (refresh_token.equals("blabla")) {
            throw new AppException(ErrorCode.NO_REFRESH_TOKEN);
        }
        // check valid
        Jwt decodedToken = this.securityUtils.checkValidRefreshToken(refresh_token);
        String emailUsernamePhone = decodedToken.getSubject();

        // check user by token + email
        UserAuthResponse currentUser = userServiceClient.getUserByRefreshTokenAndEmailOrUsernameOrPhone(refresh_token,
                emailUsernamePhone).getResult();

        // issue new token / set refresh token as cookies
        LoginResponse res = new LoginResponse();

        Set<Role> authorities = currentUser.getAuthorityIds().stream()
				.map(roleRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toSet());

        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getUsername(),
                authorities);
        res.setUser(userLogin);

        String access_token = this.securityUtils.createAccessToken(emailUsernamePhone, res);

        res.setAccess_token(access_token);

        // create refresh token
        String new_refresh_token = this.securityUtils.createRefreshToken(emailUsernamePhone, res);

        // update refreshToken for user
        this.userServiceClient.updateUserToken(new_refresh_token, emailUsernamePhone);

        // set cookies
        ResponseCookie resCookie = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(securityUtils.refreshTokenExpiration)
                .build();

        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage(message);
        apiResponse.setResult(res);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        String emailUsernamePhone = SecurityUtils.getCurrentUserLogin().isPresent()
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";

        if (emailUsernamePhone.equals("")) {
            throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        // update refresh token = null
        this.userServiceClient.updateUserToken(null, emailUsernamePhone);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Log out successfully!");

        // remove fresh token from cookie`
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(apiResponse);

    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserCreationRequest reqUser) {
        UserResponse resUser = userServiceClient.createUser(reqUser).getResult();

		UserUpdateOtpRequest updateUser = new UserUpdateOtpRequest();

        String otp = OtpUtil.generateOtp(6);
        updateUser.setOtp(otp);
        updateUser.setOtpGeneratedTime(Instant.now());

        try {
            emailUtil.sendOtpEmail(reqUser.getEmail(), otp);

        } catch (MessagingException e) {
            throw new AppException(ErrorCode.ERROR_EMAIL);
        }

        userServiceClient.updateUserOtp(updateUser);

        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("register successfully!")
                .result(resUser)
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        UserAuthResponse user = userServiceClient.handleGetUserByUsernameOrEmailOrPhone(email)
			.getResult();

        boolean isVerified = userServiceClient.verifyOtp(user.getId(), otp).getResult();
        if (isVerified) {
			UserUpdateOtpRequest updateUser = new UserUpdateOtpRequest();

            updateUser.setActive(true);
			userServiceClient.updateUserOtp(updateUser);

            return ApiResponse.<Void>builder()
                    .code(1000)
                    .message("Account verified successfully!")
                    .build();
        } else {
            return ApiResponse.<Void>builder()
                    .code(1001)
                    .message("Invalid or expired OTP.")
                    .build();
        }
    }

    @PostMapping("/regenerate-otp")
    public ApiResponse<String> regenerateOtp(@RequestParam String email) {
        // UserAuthResponse user = userServiceClient.handleGetUserByUsernameOrEmailOrPhone(email)
		// 	.getResult();

        String otp = OtpUtil.generateOtp(6);
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.ERROR_EMAIL);
        }

		UserUpdateOtpRequest updateUser = new UserUpdateOtpRequest();

        updateUser.setOtp(otp);
        updateUser.setOtpGeneratedTime(Instant.now());
		userServiceClient.updateUserOtp(updateUser);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("New OTP sent to email.")
                .result("OTP regenerated")
                .build();
    }

}
