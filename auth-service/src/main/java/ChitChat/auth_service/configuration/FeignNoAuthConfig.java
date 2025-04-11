package ChitChat.auth_service.configuration;

import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignNoAuthConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", (String) null); // Remove token
    }
}

