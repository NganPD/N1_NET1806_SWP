package online.be.api;

import online.be.entity.Account;
import online.be.model.Request.AccountRequest;
import online.be.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountManagementAPI {
    @Autowired
    AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(AccountRequest accountRequest){
        Account account = accountService.createAccount(accountRequest);
        return ResponseEntity.ok(account);
    }

//    @PostMapping("/update")
//    public ResponseEntity<Account> updateAccount(AccountRequest accountRequest){
//        Account account = accountService.updateAccount(accountRequest);
//        return ResponseEntity.ok(account);
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBooking(@PathVariable long id){
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
