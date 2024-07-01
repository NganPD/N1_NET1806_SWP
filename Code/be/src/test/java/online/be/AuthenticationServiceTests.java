package online.be;

import com.google.firebase.auth.FirebaseAuth;
import online.be.entity.Account;
import online.be.enums.Role;

import online.be.exception.BadRequestException;
import online.be.model.EmailDetail;

import online.be.exception.AuthException;
import online.be.exception.BadRequestException;
import online.be.model.EmailDetail;
import online.be.model.Request.RegisterRequest;

import online.be.model.Request.ResetPasswordRequest;
import online.be.repository.AuthenticationRepository;
import online.be.service.AuthenticationService;
import online.be.service.EmailService;
import online.be.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private EmailService emailService;

    @Mock
    private FirebaseAuth firebaseAuth;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }
    @ParameterizedTest
    @CsvFileSource(resources = "/forgot_password.csv", numLinesToSkip = 1)
    void testForgotPasswordRequest_SuccessfulSendRequest(
            long id, String fullName, String email, String phone, String password, String role
    ) {
        // Create the Account object using data from CSV
        Account account = new Account();
        account.setId(id);
        account.setFullName(fullName);
        account.setEmail(email);
        account.setPhone(phone);
        account.setPassword(password);
        account.setRole(Role.valueOf(role));

        // Mock the repository to return the account
        when(authenticationRepository.findAccountByEmail(email)).thenReturn(account);

        // Mock the token service to return a token
        when(tokenService.generateToken(any(Account.class))).thenReturn("result");

        // Run the test
        authenticationService.forgotPasswordRequest(email);

        // Verify emailService interaction
        verify(emailService, times(1)).sendMailTemplate(any(EmailDetail.class));

        // Reset the mock for the next iteration
        reset(authenticationRepository);
        reset(emailService);
    }
    @Test
    void testForgotPassword_EmailNotFound() {
        String email = "nonexistent@example.com";

        // Mock the repository to return null
        when(authenticationRepository.findAccountByEmail(email)).thenReturn(null);

        // Run the test and verify exception
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.forgotPasswordRequest(email);
        });

        assertEquals("Account not found!", exception.getMessage());

        // Verify no interaction with emailService
        verify(emailService, times(0)).sendMailTemplate(any(EmailDetail.class));
    }


    @Test
    void testResetPassword_SuccessfulPasswordChange() {
        String email = "user@example.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        // Create the Account object
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(oldPassword));

        // Mock the security context to return the current account
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(account);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the repository to return the account
        when(authenticationRepository.save(any(Account.class))).thenReturn(account);

        // Mock the password encoder
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // Create the ResetPasswordRequest
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setPassword(newPassword);

        // Run the test
        Account updatedAccount = authenticationService.resetPassword(resetPasswordRequest);

        // Verify password change
        assertEquals("encodedNewPassword", updatedAccount.getPassword());

        // Verify repository interaction
        verify(authenticationRepository, times(1)).save(any(Account.class));

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/forgot_password.csv", numLinesToSkip = 1)
    void testForgotPasswordRequest_SuccessfulSendRequest(
            long id, String fullName, String email, String phone, String password, String role
    ) {
        // Create the Account object using data from CSV
        Account account = new Account();
        account.setId(id);
        account.setFullName(fullName);
        account.setEmail(email);
        account.setPhone(phone);
        account.setPassword(password);
        account.setRole(Role.valueOf(role));

        // Mock the repository to return the account
        when(authenticationRepository.findAccountByEmail(email)).thenReturn(account);

        // Mock the token service to return a token
        when(tokenService.generateToken(any(Account.class))).thenReturn("result");

        // Run the test
        authenticationService.forgotPasswordRequest(email);

        // Verify emailService interaction
        verify(emailService, times(1)).sendMailTemplate(any(EmailDetail.class));

        // Reset the mock for the next iteration
        reset(authenticationRepository);
        reset(emailService);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/forgot_password_not_found.csv", numLinesToSkip = 1)
    void testForgotPassword_EmailNotFound(String email) {
        // Mock the repository to return null
        when(authenticationRepository.findAccountByEmail(email)).thenReturn(null);

        // Run the test and verify exception
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.forgotPasswordRequest(email);
        });

        assertEquals("Account not found!", exception.getMessage());

        // Verify no interaction with emailService
        verify(emailService, times(0)).sendMailTemplate(any(EmailDetail.class));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/reset_password.csv", numLinesToSkip = 1)
    void testResetPassword_SuccessfulPasswordChange(String email, String oldPassword, String newPassword) {
        // Create the Account object
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(oldPassword));

        // Mock the security context to return the current account
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(account);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the repository to return the account
        when(authenticationRepository.save(any(Account.class))).thenReturn(account);

        // Mock the password encoder
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // Create the ResetPasswordRequest
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setPassword(newPassword);

        // Run the test
        Account updatedAccount = authenticationService.resetPassword(resetPasswordRequest);

        // Verify password change
        assertEquals("encodedNewPassword", updatedAccount.getPassword());

        // Verify repository interaction
        verify(authenticationRepository, times(1)).save(any(Account.class));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/register_success.csv", numLinesToSkip = 1)
    void testRegister_SuccessfulRegistration(String email, String fullName, String password, String phone) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setFullName(fullName);
        registerRequest.setPassword(password);
        registerRequest.setPhone(phone);

        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setFullName(registerRequest.getFullName());
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        account.setPhone(registerRequest.getPhone());
        account.setRole(Role.CUSTOMER);

        when(authenticationRepository.save(any(Account.class))).thenReturn(account);

        Account registeredAccount = authenticationService.register(registerRequest);

        assertEquals(registerRequest.getEmail(), registeredAccount.getEmail());
        assertEquals(registerRequest.getFullName(), registeredAccount.getFullName());
        verify(emailService, times(1)).sendMailTemplate(any(EmailDetail.class));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/register_duplicate_email.csv", numLinesToSkip = 1)
    void testRegister_DuplicateEmail(String email, String fullName, String password, String phone) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setFullName(fullName);
        registerRequest.setPassword(password);
        registerRequest.setPhone(phone);

        when(authenticationRepository.save(any(Account.class)))
                .thenThrow(new DataIntegrityViolationException("account.UK_q0uja26qgu1atulenwup9rxyr"));

        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.register(registerRequest);
        });

        assertEquals("duplicate email", exception.getMessage());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/register_duplicate_phone.csv", numLinesToSkip = 1)
    void testRegister_DuplicatePhone(String email, String fullName, String password, String phone) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setFullName(fullName);
        registerRequest.setPassword(password);
        registerRequest.setPhone(phone);

        when(authenticationRepository.save(any(Account.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate phone"));

        AuthException exception = assertThrows(AuthException.class, () -> {
            authenticationService.register(registerRequest);
        });

        assertEquals("duplicate phone", exception.getMessage());

    }
}
