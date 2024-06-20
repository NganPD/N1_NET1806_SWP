package online.be.model.Request;

import lombok.Data;

@Data
public class AccountRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String role;
}
