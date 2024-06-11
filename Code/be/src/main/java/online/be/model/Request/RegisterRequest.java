package online.be.model.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import online.be.enums.Role;

@Getter
@Setter
@Data
public class RegisterRequest {
    String phone;
    String password;
    String fullName;
    String email;
    Role role;
}
