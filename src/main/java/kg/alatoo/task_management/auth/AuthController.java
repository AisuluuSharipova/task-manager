package kg.alatoo.task_management.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.entities.VerificationToken;
import kg.alatoo.task_management.enums.Role;
import kg.alatoo.task_management.repositories.UserRepository;
import kg.alatoo.task_management.repositories.VerificationTokenRepository;
import kg.alatoo.task_management.security.JwtService;
import kg.alatoo.task_management.services.EmailService;
import kg.alatoo.task_management.services.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    @PostMapping("/register")
    @Operation(summary = "Register and send verification email")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws MessagingException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .expiryDate(Instant.now().plusSeconds(86400))
                .user(user)
                .used(false)
                .build();
        tokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), verificationLink);

        return ResponseEntity.ok("Check your email to verify your account");
    }

    @GetMapping("/verify")
    @Operation(summary = "Email verification")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Verified"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        return verificationTokenService.findByToken(token).map(t -> {
            if (t.isUsed() || t.getExpiryDate().isBefore(Instant.now())) {
                return ResponseEntity.badRequest().body("Invalid or expired token");
            }

            User user = t.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            System.out.println("Email verified for user: " + user.getEmail());


            verificationTokenService.markTokenAsUsed(t);
            return ResponseEntity.ok("Email successfully verified!");
        }).orElse(ResponseEntity.badRequest().body("Token not found"));

    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = (User) auth.getPrincipal();
        String accessToken = jwtService.generateToken(user, false);
        String refreshToken = jwtService.generateToken(user, true);
        return new AuthResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<AuthResponse> refresh(@RequestBody TokenRefreshRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            String username = jwtService.extractUsername(refreshToken);
            User user = (User) userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(refreshToken, user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String newAccessToken = jwtService.generateToken(user, false);
            String newRefreshToken = jwtService.generateToken(user, true);

            return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("You have successfully logged out");
    }
}
