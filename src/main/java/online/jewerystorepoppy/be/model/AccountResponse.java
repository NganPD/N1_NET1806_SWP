package online.jewerystorepoppy.be.model;

import lombok.Data;
import online.jewerystorepoppy.be.entity.Account;

@Data
public class AccountResponse extends Account {
    String token;
}
