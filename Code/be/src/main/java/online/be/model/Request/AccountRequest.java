package online.be.model.Request;

import lombok.Data;
import online.be.enums.Role;

@Data
public class AccountRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private Role role;
}
