package online.be.service;

import online.be.entity.Account;
import online.be.enums.Role;
import online.be.exception.AuthException;
import online.be.exception.BadRequestException;
import online.be.model.EmailDetail;
import online.be.model.Request.AccountRequest;
import online.be.model.Response.AccountResponse;
import online.be.repository.AccountRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.rmi.server.LogStream.log;

@Service
public class AccountService{

    @Autowired
    private AccountRepostory accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public Account createAccount(AccountRequest accountRequest) {
        //kiểm tra xem là tài khoản đã tồn tại hay chưa
        Account existingAccount = accountRepository.findAccountByEmail(accountRequest.getEmail());
        if(existingAccount != null){
            throw new AuthException("Account is exist!");
        }
        Account account = new Account();
        account.setFullName(accountRequest.getFullName());
        account.setEmail(accountRequest.getEmail());
        account.setPhone(accountRequest.getPhone());
        account.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        account.setActive(true);
        switch (accountRequest.getRole()) {
            case MANAGER:
                account.setRole(Role.MANAGER);
                break;
            case STAFF:
                account.setRole(Role.STAFF);
                break;
            case ADMIN:
                account.setRole(Role.ADMIN);
                break;
        }
        try {
            account = accountRepository.save(account);
        }catch (DataIntegrityViolationException e){
            System.out.println(e.getMessage());
            if(e.getMessage().contains("account.UK_q0uja26qgu1atulenwup9rxyr"))
                throw new AuthException("duplicate email");
            else{ throw new AuthException("duplicate phone");}
        }
        try {
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setRecipient(account.getEmail());
            emailDetail.setSubject("You are invited to system!");
            emailDetail.setMsgBody("Welcome to our platform. Your account has been successfully created.");
            emailDetail.setFullName(accountRequest.getFullName());
            emailDetail.setButtonValue("Login to system");
            emailDetail.setLink("http://goodminton.online/");
            emailService.sendMailTemplate(emailDetail);
        } catch (Exception e) {
            String msg = e.getMessage();
            log(msg);
        }
        return account;
    }

    public Account deleteById(long id) {
        Account account = accountRepository.findById(id).orElseThrow(
                ()-> new BadRequestException("Account does not exist!")
        );
        account.setActive(false);
        return accountRepository.save(account);
    }

//    public Account createAdminAccount (AccountRequest accountRequest){
//        //kiểm tra xem email đã tồn tại hay chưa
//
//        Account existingAccount = accountRepository.findAccountByEmail(accountRequest.getEmail());
//        if(existingAccount != null){
//            throw new BadRequestException("Email is already in use");
//        }
//
//        Account admin = new Account();
//        admin.setFullName(accountRequest.getFullName());
//        admin.setEmail(accountRequest.getEmail());
//        admin.setPhone(accountRequest.getPhone());
//        admin.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
//        admin.setActive(true); // Mặc định tài khoản admin là active
//        admin.setRole(Role.ADMIN); // Đặt vai trò của tài khoản là ADMIN
//
//        // Lưu thông tin admin xuống cơ sở dữ liệu
//        try {
//            admin = accountRepository.save(admin);
//        } catch (DataIntegrityViolationException e) {
//            if(e.getMessage().contains("account.UK_dgdnj692f2g5ebicy1xyc2l3w")){
//                throw new AuthException("Duplicate Phone Number");
//            }
//        }
//
//        return admin;
//    }

}
