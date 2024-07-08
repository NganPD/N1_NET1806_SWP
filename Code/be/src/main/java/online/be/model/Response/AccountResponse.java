package online.be.model.Response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import online.be.entity.Account;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountResponse extends Account {
    String token;
}
