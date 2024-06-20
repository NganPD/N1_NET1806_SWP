package online.be.model.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String role;
}
