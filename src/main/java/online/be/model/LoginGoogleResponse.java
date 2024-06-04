package online.be.model;

import lombok.Getter;
import lombok.Setter;
import online.be.enums.Role;

@Getter
@Setter
public class LoginGoogleResponse {

    private long id;

    private String fullName;

    private String email;

    private String phone;

    private String password;

    private Role role;

    private String token;
}
