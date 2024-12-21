package ChitChat.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import ChitChat.auth_service.entity.Role;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginResponse {
     @JsonProperty("access_token")
     private String access_token;
     private UserLogin user;

     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     public static class UserLogin {

          public UserLogin(long id, String email, String username, Set<Role> authorities) {
               this.id = id;
               this.email = email;
               this.username = username;
               this.authorities = authorities;
          }

          private long id;
          private String email;
          private String username;
          private String location;
          private String bio;
          private boolean isActive;
          private Set<Role> authorities;

          public UserLogin(Long id, String email, String username, String location, String bio, 
                              boolean isActive, Set<Role> authorities) {
               this.id = id;
               this.email = email;
               this.username = username;
               this.location = location;
               this.bio = bio;
               this.isActive = isActive;
               this.authorities = authorities;
          }
     }

     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     public static class UserGetAccount {
          private UserLogin user;
     }

     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     public static class UserInsideToken {
          private long id;
          private String email;
          private String username;
          private String location;
     }
}
