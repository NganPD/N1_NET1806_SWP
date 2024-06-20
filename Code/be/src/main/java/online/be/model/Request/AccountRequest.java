package online.be.model.Request;

<<<<<<< HEAD
import lombok.Data;

@Data
=======
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
>>>>>>> origin/feat/AccountManagerAPI
public class AccountRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String role;
}
