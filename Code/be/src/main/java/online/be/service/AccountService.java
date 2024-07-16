
package online.be.service;

import online.be.entity.Account;
import online.be.entity.Venue;
import online.be.entity.Wallet;
import online.be.enums.Role;
import online.be.exception.AuthException;
import online.be.exception.BadRequestException;
import online.be.exception.ResourceNotFoundException;
import online.be.model.EmailDetail;
import online.be.model.Request.AccountRequest;
import online.be.model.Request.UpdatedAccountRequest;
import online.be.repository.AccountRepostory;
import online.be.repository.VenueRepository;
import online.be.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.rmi.server.LogStream.log;

@Service
public class AccountService {
    @Autowired
    AccountRepostory accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    WalletRepository walletRepository;

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
        Wallet wallet = new Wallet();
        wallet.setBalance(0);
        wallet.setAccount(account);

        switch (accountRequest.getRole()) {
            case MANAGER:
                account.setRole(Role.MANAGER);
                break;
            case STAFF:
                account.setRole(Role.STAFF);
                break;
            default:
                throw new BadRequestException("Role is invalid. Please enter STAFF or MANAGER");
        }
        try {
            wallet = walletRepository.save(wallet);
            account.setWallet(wallet);
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
            throw new RuntimeException(msg);
        }
        return account;
    }

    public Account deActiveAccount(long id) {
        Account account = accountRepository.findById(id).orElseThrow(
                ()-> new BadRequestException("Account does not exist!")
        );
        account.setActive(false);
        return accountRepository.save(account);
    }

    public Account activeAccount(long id){
        Account account = accountRepository.findById(id).get();
        if(account != null){
            account.setActive(true);
            return accountRepository.save(account);
        }else{
            throw new BadRequestException("Account not found");
        }
    }

    public void assignRole(long accountId, Role role){
        Account account = accountRepository.findById(accountId).get();
        if(account == null){
            throw new AuthException("Account not found");
        }
        account.setRole(role);
        accountRepository.save(account);
    }

    public List<Account> getByRole(Role role){
        return accountRepository.findByRole(role);
    }

    public Account updateAccount(UpdatedAccountRequest updatedAccountRequest, long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BadRequestException("Account Not Found"));
        account.setPhone(updatedAccountRequest.getPhone());
        account.setEmail(updatedAccountRequest.getEmail());
        account.setFullName(updatedAccountRequest.getFullname());
        //save
        return accountRepository.save(account);
    }

    //số lượng người dùng trong hệ thống
    public int numberOfCustomerAccount(){
        List<Account> customers = accountRepository.findByRole(Role.CUSTOMER);
        return customers.size();
    }
}
