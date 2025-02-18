package ChitChat.user_service.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ChitChat.user_service.entity.User;
import ChitChat.user_service.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {
        initAdminAccount();
    }

    private void initAdminAccount() {
        String adminUsername = "ADMIN";
        String adminEmail = "admin@chitchat.com";
        String defaultPassword = "adminPassword";
        String firstName = "Admin";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            Set<String> adminRoles = new HashSet<>();
            String adminRole = "ADMIN";
            String userRole = "USER";
            adminRoles.add(adminRole);
            adminRoles.add(userRole);

            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode(defaultPassword));
            adminUser.setActive(true);
            adminUser.setAuthorities(adminRoles);
            adminUser.setFirstName(firstName);

            userRepository.save(adminUser);
            log.info("Admin account '{}' created successfully!", adminUsername);
        } else {
            log.info("Admin account '{}' already exists.", adminUsername);
        }
    }

}

