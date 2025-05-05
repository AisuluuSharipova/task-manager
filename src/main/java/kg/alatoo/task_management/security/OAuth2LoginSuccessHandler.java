package kg.alatoo.task_management.security;

import jakarta.transaction.Transactional;
import kg.alatoo.task_management.entities.RefreshToken;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.enums.Role;
import kg.alatoo.task_management.repositories.RefreshTokenRepository;
import kg.alatoo.task_management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = User.builder()
                    .username(name)
                    .email(email)
                    .password("") // соц. логины не используют пароль
                    .role(Role.USER)
                    .enabled(true)
                    .build();
            userRepository.save(user);
        }

        // Удаляем старый refresh token
        refreshTokenRepository.deleteByUser(user);

        // Генерация токенов
        String accessToken = jwtService.generateToken(user, false);
        String refreshTokenString = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenString)
                .expiryDate(Instant.now().plusSeconds(604800))
                .user(user)
                .build();
        refreshTokenRepository.save(refreshToken);

        // ...
// после сохранения refresh token'а
        response.setContentType("application/json");
        response.getWriter().write("{\"accessToken\": \"" + accessToken + "\", \"refreshToken\": \"" + refreshTokenString + "\"}");

    }
}
