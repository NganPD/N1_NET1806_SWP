package online.be.model.Request;

import lombok.Data;
import online.be.enums.Role;


@Data
public class UpdatedAccountRequest {
    private String fullname;
    private String phone;
    private String email;
}
