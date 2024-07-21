package online.be.repository;

import online.be.entity.Account;
import online.be.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.email = :identifier OR a.phone = :identifier")
    Account findAccountByEmailOrPhone(@Param("identifier") String identifier);
    Account findUserById(long id);
    List<Account> findStaffByStaffVenue_Id(long venueId);
    Account findManagerByAssignedVenue_Id(long venueId);
    @Query("SELECT a FROM Account a WHERE a.role = 'ADMIN'")
    List<Account> findAdmin();
    List<Account> findByRole(Role role);

    Account findAccountByEmail(String email);
    List<Account> findByRoleAndAssignedVenueIsNull(Role role);
}
