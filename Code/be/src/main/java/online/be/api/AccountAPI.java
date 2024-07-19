package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Account;
import online.be.entity.Booking;
import online.be.enums.Role;
import online.be.model.Request.AccountRequest;
import online.be.model.Request.UpdatedAccountRequest;
import online.be.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class AccountAPI {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest accountRequest) {
        Account account = accountService.createAccount(accountRequest);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> lockAccount(@PathVariable long accountId) {
        accountService.deActiveAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{accountId}/activate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Account> activeAccount(@PathVariable long accountId) {
        Account account = accountService.activeAccount(accountId);
        return ResponseEntity.ok(account);
    }

    @PatchMapping("/{accountId}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> assignRole(@PathVariable long accountId, @RequestParam Role role) {
        accountService.assignRole(accountId, role);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<Account>> getByRole(@PathVariable Role role) {
        List<Account> accounts = accountService.getByRole(role);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{accountId}/update")
    public ResponseEntity<Account> updateAccount(@PathVariable long accountId, @RequestBody UpdatedAccountRequest request) {
        Account updatedAccount = accountService.updateAccount(request, accountId);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/number-customers")
    public ResponseEntity getNumberOfCourts() {
        int numberOfCustomers = accountService.numberOfCustomerAccount();
        return ResponseEntity.ok(numberOfCustomers);
    }

    @GetMapping("/{identifierString}/find-account-by-identifier")
    public ResponseEntity getAccountByIdentifier(@PathVariable String identifierString) {
        Account account = accountService.findAccountByEmailOrPhone(identifierString);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountId}/bookings")
    public ResponseEntity<List<Booking>> getBookingsByAccountId(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId); // Create a getAccountById method in AccountService
        return ResponseEntity.ok(account.getBookings());
    }
}
