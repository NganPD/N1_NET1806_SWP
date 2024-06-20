package online.be.repository;

import online.be.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByPhone(String phone);
    Account findAccountByEmail(String email);
}
