package ChitChat.auth_service.entity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ChitChat.auth_service.dto.response.UserAuthResponse;
import ChitChat.auth_service.exception.AppException;
import ChitChat.auth_service.exception.ErrorCode;
import ChitChat.auth_service.repository.RoleRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class UserDetailsCustom implements UserDetailsService {

    private final UserServiceClient userServiceClient;
	private final RoleRepository roleRepository; 

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
			UserAuthResponse userDto = userServiceClient.handleGetUserByUsernameOrEmailOrPhone(login).getResult();

			Set<SimpleGrantedAuthority> authorities = userDto.getAuthorityIds().stream()
				.map(roleRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(Role::getAuthority)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());

			return new org.springframework.security.core.userdetails.User(
					userDto.getEmail(),
					userDto.getPassword(),
					authorities
				);
        } catch (FeignException.NotFound e) {
            log.info("User not found in user-service: {}", login);
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
    }

}
