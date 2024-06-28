package online.be.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import online.be.entity.Account;
import online.be.enums.Role;
import online.be.exception.AuthException;
import online.be.exception.BadRequestException;
import online.be.model.EmailDetail;
import online.be.model.Request.*;
import online.be.model.Response.AccountResponse;
import online.be.model.Response.LoginGoogleResponse;
import online.be.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    public Account register(RegisterRequest registerRequest) {
        Account existingAccount = authenticationRepository.findAccountByEmail(registerRequest.getEmail());
        if (existingAccount != null) {
            if (!existingAccount.isActive()) {
                throw new BadRequestException("The account has been locked, please contact the admin to unlock the account.");
            } else {
                throw new AuthException("Duplicate email");
            }
        }
        Account account = new Account();
        account.setPhone(registerRequest.getPhone());
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        account.setRole(Role.CUSTOMER);
        account.setActive(true);
        account.setEmail(registerRequest.getEmail());
        account.setFullName(registerRequest.getFullName());

        try {
            account = authenticationRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("account.UK_q0uja26qgu1atulenwup9rxyr"))
                throw new AuthException("duplicate email");
            else{
                throw new AuthException("duplicate phone");
            }
        }

        try {
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setRecipient(account.getEmail());
            emailDetail.setSubject("You are invited to system!");
            emailDetail.setMsgBody("aaa");
            emailDetail.setFullName(registerRequest.getFullName());
            emailDetail.setButtonValue("Login to system");
            emailDetail.setLink("http://goodminton.online/");
            emailService.sendMailTemplate(emailDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;
    }

    public AccountResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
        } catch (Exception e) {
            throw new AuthException("Wrong Username or Password");
        }

        //=> account chuẩn
        Account account = authenticationRepository.findAccountByEmail(loginRequest.getEmail());
        if (account == null) {
            throw new AuthException("Account not found");
        }
        String token = tokenService.generateToken(account);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setPhone(account.getPhone());
        accountResponse.setToken(token);
        accountResponse.setEmail(account.getEmail());
        accountResponse.setFullName(account.getFullName());
        accountResponse.setRole(account.getRole());
        accountResponse.setId(account.getId());
        return accountResponse;
    }

    public LoginGoogleResponse loginGoogle(LoginGoogleRequest loginRequest) {
        LoginGoogleResponse accountResponse = new LoginGoogleResponse();
        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(loginRequest.getToken());
            String email = firebaseToken.getEmail();
            Account account = authenticationRepository.findAccountByEmail(email);
            if (account == null) {
                account = new Account();
                account.setFullName(firebaseToken.getName());
                account.setEmail(firebaseToken.getEmail());
                account.setActive(true);
                account.setRole(Role.CUSTOMER);
                authenticationRepository.save(account);
            }
            accountResponse.setEmail(account.getEmail());
            accountResponse.setFullName(account.getFullName());
            accountResponse.setId(account.getId());
            accountResponse.setRole(account.getRole());

            String token = tokenService.generateToken(account);
            accountResponse.setToken(token);

        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
            throw new AuthException("Firebase Authentication failed. Please try again later");
        } catch(Exception e) {
            e.printStackTrace();
            throw new AuthException("An unexpected error occurred! Please try again later");
        }
        return accountResponse;
    }

    public void forgotPasswordRequest(String email) {
        Account account = authenticationRepository.findAccountByEmail(email);
        if (account == null) {
            throw new BadRequestException("Account not found!");
        }

        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(account.getEmail());
        emailDetail.setFullName(account.getFullName());
        emailDetail.setSubject("Reset password for account " + account.getEmail() + "!");
        emailDetail.setMsgBody("aaa");
        emailDetail.setButtonValue("Reset Password");
        emailDetail.setLink("http://goodminton.online/reset-password?token=" + tokenService.generateToken(account));
        emailService.sendMailTemplate(emailDetail);
    }

    public Account resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Account account = getCurrentAccount();
        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        return authenticationRepository.save(account);
    }

    public List<Account> getAllAccount() {
        return authenticationRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authenticationRepository.findAccountByEmail(email);
    }

    public Account getCurrentAccount() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Account updateAccount(UpdatedAccountRequest updatedAccountRequest) {
        Account account = authenticationRepository.findById(updatedAccountRequest.getAccountId())
                .orElseThrow(() -> new BadRequestException("Account Not Found"));
        account.setPhone(updatedAccountRequest.getPhone());
        account.setEmail(updatedAccountRequest.getEmail());
        account.setFullName(updatedAccountRequest.getFullname());
        account.setRole(updatedAccountRequest.getRole());
        return authenticationRepository.save(account);
    }

    public void assignRole(Long accountId, Role role) {
        Account account = authenticationRepository.findById(accountId).orElseThrow(
                () -> new BadRequestException("Account not found!")
        );
        account.setRole(role);
        authenticationRepository.save(account);
    }

    public void lockAccount(Long accountId) {
        Account account = authenticationRepository.findById(accountId).orElseThrow(
                () -> new BadRequestException("Account not found!")
        );
        account.setActive(false);
        authenticationRepository.save(account);
    }

    public void unlockAccount(long accountId) {
        Account account = authenticationRepository.findById(accountId).orElseThrow(
                () -> new BadRequestException("Account not found!")
        );
        account.setActive(true);
        authenticationRepository.save(account);
    }

}
