package online.be.repository;

import online.be.entity.Wallet;
import online.be.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Wallet findWalletByAccount_Id(long accountId);

    Wallet findWalletByAccountRole(Role role);
    Wallet findWalletByWalletID(long id);
}