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

    @DeleteMapping("/account/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteAccount(@PathVariable long id){
        Account deletedAccount = accountService.deActiveAccount(id);
        return ResponseEntity.ok(deletedAccount);
    }

    @PatchMapping("/{id}")
    public ResponseEntity activeAccount(@PathVariable long id){
        Account account = accountService.activeAccount(id);
        return ResponseEntity.ok(account);
    }


}
