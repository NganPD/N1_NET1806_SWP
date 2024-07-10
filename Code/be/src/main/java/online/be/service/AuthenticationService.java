
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
    AuthenticationManager authenticationManager;

    // xử lý logic
    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;

    public Account register(RegisterRequest registerRequest) {
        //registerRequest: thông tin ngừoi dùng yêu cầu

        // xử lý logic register
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
            else {
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
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // nhờ repo => save xuống db
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
        // => account chuẩn
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
        accountResponse.setActive(account.isActive());
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

            //sinh ra token và gán vảo response
            String token = tokenService.generateToken(account);
            accountResponse.setToken(token);

        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();// Tự handle exception để front end hiểu.
            throw new AuthException("Firebase Authentication failed. Please try again later");
        }catch(Exception e){
            //các ngoại lệ khác
            e.printStackTrace();
            throw new AuthException("An unexpected error occured! Please try again later");
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
        // Kiểm tra account có phải là instance của Account không
        if (!(account instanceof Account)) {
            throw new ClassCastException("Đối tượng không phải là Account");
        }
        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        return authenticationRepository.save(account);
    }

    public Account getCurrentAccount() {
        Object currentAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();// lấy đối tượng tài khoản hiện tại từ session hoặc context
        if (currentAccount instanceof Account) {
            return (Account) currentAccount;
        } else {
            throw new ClassCastException("Đối tượng không phải là Account");
        }
    }


    public List<Account> getAllAccount() {
        return authenticationRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authenticationRepository.findAccountByEmail(email);
    }


    public Account updateAccount(UpdatedAccountRequest updatedAccountRequest) {
        Account account = authenticationRepository.findById(updatedAccountRequest.getAccountId())
                .orElseThrow(() -> new BadRequestException("Account Not Found"));
        account.setPhone(updatedAccountRequest.getPhone());
        account.setEmail(updatedAccountRequest.getEmail());
        account.setFullName(updatedAccountRequest.getFullname());
        account.setRole(updatedAccountRequest.getRole());
        //save
        return authenticationRepository.save(account);
    }

}
