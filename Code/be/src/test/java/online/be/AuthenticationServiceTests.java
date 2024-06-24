package online.be;

import online.be.entity.Account;
import online.be.enums.Role;
import online.be.exception.AuthException;
import online.be.model.Request.LoginRequest;
import online.be.model.Response.AccountResponse;
import online.be.repository.AccountRepostory;
import online.be.repository.AuthenticationRepository;
import online.be.service.AuthenticationService;
import online.be.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceTests.class);
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Account account;
    private String token;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setEmail("test@example.com");
        account.setPhone("1234567890");
        account.setFullName("Test User");
        account.setPassword("Password");
        account.setRole(Role.CUSTOMER);
        account.setId(1L);

        token = "mockToken";
    }

    @Test
    void testLogin_AuthenticationFailure() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrong password");

        // Mock authentication failure
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthException("Wrong Username or Password"));

        // Act and assert
        AuthException exception = assertThrows(AuthException.class, () ->
                authenticationService.login(loginRequest));

        assertEquals("Wrong Username or Password", exception.getMessage());

        // Verify interactions
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authenticationRepository, never()).findAccountByEmail(anyString());
        verify(tokenService, never()).generateToken(any(Account.class));
        System.out.println("Wrong Username or Password");
    }
    @Test
    void testLogin_AuthenticationSuccess(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password");

        when(authenticationRepository.findAccountByEmail(loginRequest.getEmail())).thenReturn(account);
        when(tokenService.generateToken(account)).thenReturn(token);

        //Act
        AccountResponse accountResponse = authenticationService.login(loginRequest);

        //Assert
        assertNotNull(accountResponse);
        assertEquals(accountResponse.getEmail(), account.getEmail());
        assertEquals(accountResponse.getFullName(), account.getFullName());
        assertEquals(accountResponse.getPhone(), account.getPhone());
        assertEquals(accountResponse.getToken(), token);
        assertEquals(accountResponse.getRole(), account.getRole());
        assertEquals(accountResponse.getId(), account.getId());

        verify(authenticationRepository, times(1)).findAccountByEmail(loginRequest.getEmail());
        verify(tokenService, times(1)).generateToken(account);
        System.out.println("Login Successfully");
    }

    @Test
    public void testLogin_AccountNotFound(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password");

        when(authenticationRepository.findAccountByEmail("nonexistent@example.com"))
                .thenReturn(null);

        AuthException thrown = assertThrows(AuthException.class, ()->{
            authenticationService.login(loginRequest);
        });

        assertEquals("Account not found", thrown.getMessage());
    }
}
