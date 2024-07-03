package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Account;
import online.be.model.Request.AccountRequest;
import online.be.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class AccountAPI {
    @Autowired
    AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity createAccount(@RequestBody AccountRequest accountRequest){
        Account account = accountService.createAccount(accountRequest);
        return ResponseEntity.ok(account);
    }

//    @PostMapping("/update")
//    public ResponseEntity<Account> updateAccount(AccountRequest accountRequest){
//        Account account = accountService.updateAccount(accountRequest);
//        return ResponseEntity.ok(account);
//    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteBooking(@PathVariable long id){
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteAccount(@PathVariable long id){
        Account deletedAccount = accountService.deleteById(id);
        return ResponseEntity.ok(deletedAccount);
    }
}
