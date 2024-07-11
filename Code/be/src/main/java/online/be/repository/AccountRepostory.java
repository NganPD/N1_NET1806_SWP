package online.be.repository;

import online.be.entity.Account;
import online.be.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepostory extends JpaRepository<Account, Long> {
    Account findAccountByEmail(String email);
    Account findUserById(long id);
    List<Account> findStaffByStaffVenue_Id(long venueId);
    Account findManagerByAssignedVenue_Id(long venueId);
    @Query("SELECT a FROM Account a WHERE a.role = 'ADMIN'")
    Account findAdmin();
    List<Account> findByRole(Role role);
}
