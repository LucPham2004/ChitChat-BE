package ChitChat.auth_service.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import ChitChat.auth_service.entity.Role;
import ChitChat.auth_service.repository.RoleRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("USER");
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByAuthority(roleName).isEmpty()) {
            Role role = new Role();
            role.setAuthority(roleName);
            roleRepository.save(role);
            log.info("Role '{}' created successfully!", roleName);
        } else {
            log.info("Role '{}' already exists.", roleName);
        }
    }
}

