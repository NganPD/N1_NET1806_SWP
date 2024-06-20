package online.be.model.Request;

import lombok.Data;

@Data
public class UpdateAccountRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
}
