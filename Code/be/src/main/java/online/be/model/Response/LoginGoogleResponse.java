package online.be.model.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.entity.Wallet;
import online.be.enums.Role;

@Getter
@Setter
@ToString

public class LoginGoogleResponse {

    private long id;

    private String fullName;

    private String email;

    private String phone;

    private String password;

    private Role role;

    private Wallet wallet;
    private String token;
}
