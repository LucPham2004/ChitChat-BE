package ChitChat.chat_service;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableFeignClients
public class ChatServiceApplication {
	
	@PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    }

	public static void main(String[] args) {
		SpringApplication.run(ChatServiceApplication.class, args);
	}

}
