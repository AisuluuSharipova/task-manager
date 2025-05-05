package kg.alatoo.task_management.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import kg.alatoo.task_management.security.JwtService;
import kg.alatoo.task_management.services.EmailService;
import kg.alatoo.task_management.services.VerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private JwtService jwtService;
    @MockBean private AuthenticationManager authManager;
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private EmailService emailService;
    @MockBean private VerificationTokenService verificationTokenService;
    @MockBean private kg.alatoo.task_management.repositories.UserRepository userRepository;
    @MockBean private kg.alatoo.task_management.repositories.VerificationTokenRepository verificationTokenRepository;
    @MockBean private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");

        authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password");
    }

    @Test
    void registerUser_shouldReturnOk() throws Exception {
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.empty());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void loginUser_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk());
    }
}
