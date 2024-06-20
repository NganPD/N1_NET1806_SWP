package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.model.Request.*;
import online.be.model.Response.AccountResponse;
import online.be.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import online.be.entity.Account;
import online.be.service.AuthenticationService;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class AuthenticationAPI {

    // nhận request từ front-end

    @Autowired
    EmailService emailService;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("test") // /api/test
    public ResponseEntity test() {
        return ResponseEntity.ok("test");
    }

    @GetMapping("admin-only") // /api/admin-only
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity getAdmin() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("register") // /api/register
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        // account đã add xuống db
        Account account = authenticationService.register(registerRequest);
        return ResponseEntity.ok(account);
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        AccountResponse account = authenticationService.login(loginRequest);
        return ResponseEntity.ok(account);
    }

    @PostMapping("login-google")
    public ResponseEntity loginGoogle(@RequestBody LoginGoogleRequest loginGg){
        return ResponseEntity.ok(authenticationService.loginGoogle(loginGg));
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        authenticationService.forgotPasswordRequest(forgotPasswordRequest.getEmail());
    }

    @PatchMapping("reset-password")
    public void resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        authenticationService.resetPassword(resetPasswordRequest);
    }

    @GetMapping("account")
    public ResponseEntity getAllAccount() {
        return ResponseEntity.ok(authenticationService.getAllAccount());
    }

    @PatchMapping("lock-account")
    public void lockAccount(@PathVariable long id){
        authenticationService.lockAccount(id);
    }

    @PatchMapping("unlock-account")
    public void unlockAccount(@PathVariable long id){
        authenticationService.unlockAccount(id);
    }

    @PostMapping("update-account")
    public ResponseEntity updateAccount(@RequestBody UpdatedAccountRequest accountRequest){
        Account account = authenticationService.updateAccount(accountRequest);
        return ResponseEntity.ok(account);
    }
}
