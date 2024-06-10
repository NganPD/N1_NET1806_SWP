package online.be.model.Response;

import lombok.Data;
import online.be.entity.Account;

@Data
public class AccountResponse extends Account {
    String token;
}
