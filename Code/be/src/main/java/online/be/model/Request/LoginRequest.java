package online.be.model.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    String email;
    String password;
}
