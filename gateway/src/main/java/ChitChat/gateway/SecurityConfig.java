package ChitChat.gateway;

import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${chitchat.jwt.base64-secret}")
    private String jwtKey;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth-service/**").permitAll()
                        .pathMatchers("/auth/**").permitAll()
                        // .pathMatchers("/notification-service/**").hasRole("USER")

                        .pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        
                        .pathMatchers("/user-service/users/get**").permitAll()
                        .pathMatchers("/user-service/users/get/**").permitAll()
                        .pathMatchers("/user-service/users/search").permitAll()
                        .pathMatchers("/user-service/users/search**").permitAll()
                        .pathMatchers("/user-service/users/search/**").permitAll()
                        .pathMatchers("/user-service/users/search&token**").permitAll()
                        .pathMatchers("/user-service/users/search-ids**").permitAll()
                        .pathMatchers("/user-service/users/create**").permitAll()
                        .pathMatchers("/user-service/users/update/token**").permitAll()
                        // .pathMatchers("/user-service/users/verify-otp").permitAll()
                        // .pathMatchers("/user-service/users/update/otp").permitAll()

                        .pathMatchers("/chat-service/ws/**").permitAll()
                        .pathMatchers("/chat-service/conversations/get/id**").permitAll()

                        .pathMatchers("/user-service/**")
                            .hasAnyAuthority("ROLE_USER_CREATE", "ROLE_USER_UPDATE")
                        .pathMatchers("/chat-service/**")
                            .hasAnyAuthority("ROLE_USER_CREATE", "ROLE_USER_UPDATE")

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder())
                                .jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder
                .withSecretKey(getSecretKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("permission");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> permissions = jwt.getClaimAsStringList("permission");
            System.out.println("PERMISSIONS FROM JWT: " + permissions);

            return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        });

        return new ReactiveJwtAuthenticationConverterAdapter(authenticationConverter);
    }

    private SecretKey getSecretKey() {
        try {
            byte[] keyBytes = java.util.Base64.getDecoder().decode(jwtKey);
            if (keyBytes.length != 32) {
                throw new IllegalArgumentException("JWT key must be exactly 32 bytes for HS256.");
            }
            return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtils.JWT_ALGORITHM.getName());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid base64 secret key", e);
        }
    }
}

