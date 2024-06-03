package online.jewerystorepoppy.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import online.jewerystorepoppy.be.entity.Account;

public interface AuthenticationRepository extends JpaRepository<Account, Long> {

    Account findAccountByPhone(String phone);
    Account findAccountByEmail(String email);
}
