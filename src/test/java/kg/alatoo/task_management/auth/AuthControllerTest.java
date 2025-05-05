package kg.alatoo.task_management.auth;

import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.entities.VerificationToken;
import kg.alatoo.task_management.enums.Role;
import kg.alatoo.task_management.repositories.UserRepository;
import kg.alatoo.task_management.repositories.VerificationTokenRepository;
import kg.alatoo.task_management.security.JwtService;
import kg.alatoo.task_management.services.EmailService;
import kg.alatoo.task_management.services.VerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock private UserRepository userRepository;
    @Mock private VerificationTokenRepository tokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authManager;
    @Mock private JwtService jwtService;
    @Mock private UserDetailsService userDetailsService;
    @Mock private VerificationTokenService verificationTokenService;
    @Mock private EmailService emailService;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("test");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");

        testUser = User.builder()
                .id(1L)
                .username("test")
                .email("test@example.com")
                .password("encoded")
                .role(Role.USER)
                .enabled(false)
                .build();
    }

    @Test
    void testRegister_NewUser() throws Exception {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encoded");

        ResponseEntity<String> response = authController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Check your email"));
    }

    @Test
    void testRegister_UserAlreadyExists() throws Exception {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(testUser));

        ResponseEntity<String> response = authController.register(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void testVerifyEmail_Success() {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .expiryDate(Instant.now().plusSeconds(86400))
                .user(testUser)
                .used(false)
                .build();

        when(verificationTokenService.findByToken(token)).thenReturn(Optional.of(verificationToken));

        ResponseEntity<String> response = authController.verifyEmail(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Email successfully verified"));
    }

    @Test
    void testLogin_Success() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(testUser);
        when(authManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken(testUser, false)).thenReturn("accessToken");
        when(jwtService.generateToken(testUser, true)).thenReturn("refreshToken");

        AuthResponse response = authController.login(authRequest);

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void testRefreshToken_Valid() {
        String refreshToken = "your-refresh-token";

        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken(refreshToken);

        when(jwtService.extractUsername(refreshToken)).thenReturn(testUser.getEmail());
        when(userDetailsService.loadUserByUsername(testUser.getEmail())).thenReturn(testUser);
        when(jwtService.isTokenValid(refreshToken, testUser)).thenReturn(true);
        when(jwtService.generateToken(testUser, false)).thenReturn("newAccess");
        when(jwtService.generateToken(testUser, true)).thenReturn("newRefresh");

        ResponseEntity<AuthResponse> response = authController.refresh(refreshRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newAccess", response.getBody().getAccessToken());
        assertEquals("newRefresh", response.getBody().getRefreshToken());
    }

    @Test
    void testLogout() {
        ResponseEntity<String> response = authController.logout();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("You have successfully logged out", response.getBody());
    }
}
