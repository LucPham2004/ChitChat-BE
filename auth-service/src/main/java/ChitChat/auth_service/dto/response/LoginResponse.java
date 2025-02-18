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

          private long id;
          private String email;
          private String username;
          private String avatarUrl;
          private String firstName;
          private String lastName;
          private String location;
          private String bio;
          private Set<Role> authorities;
          
          public UserLogin(long id, String email, String username, Set<Role> authorities) {
               this.id = id;
               this.email = email;
               this.username = username;
               this.authorities = authorities;
          }
          
          public UserLogin(long id, String email, String username, String avatarUrl, String firstName, String lastName,
                    Set<Role> authorities) {
               this.id = id;
               this.email = email;
               this.username = username;
               this.avatarUrl = avatarUrl;
               this.firstName = firstName;
               this.lastName = lastName;
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
