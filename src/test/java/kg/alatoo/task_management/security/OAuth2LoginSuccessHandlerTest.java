package kg.alatoo.task_management.security;

import kg.alatoo.task_management.entities.RefreshToken;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.enums.Role;
import kg.alatoo.task_management.repositories.RefreshTokenRepository;
import kg.alatoo.task_management.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OAuth2LoginSuccessHandlerTest {

    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private JwtService jwtService;
    private OAuth2LoginSuccessHandler successHandler;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        jwtService = mock(JwtService.class);
        successHandler = new OAuth2LoginSuccessHandler(userRepository, refreshTokenRepository, jwtService);
    }

    @Test
    void shouldRegisterNewUserAndReturnTokens() throws Exception {
        String email = "test@example.com";
        String name = "Test User";
        String generatedAccessToken = "access-token";

        OAuth2User mockOAuth2User = mock(OAuth2User.class);
        when(mockOAuth2User.getAttribute("email")).thenReturn(email);
        when(mockOAuth2User.getAttribute("name")).thenReturn(name);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(jwtService.generateToken(any(User.class), eq(false))).thenReturn(generatedAccessToken);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(mockOAuth2User, null);

        successHandler.onAuthenticationSuccess(request, response, auth);

        verify(userRepository).save(any(User.class));
        verify(refreshTokenRepository).deleteByUser(any(User.class));
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(jwtService).generateToken(any(User.class), eq(false));

        String content = response.getContentAsString();
        assertTrue(content.contains("accessToken"));
        assertTrue(content.contains("refreshToken"));
        assertEquals("application/json", response.getContentType());
    }

    @Test
    void shouldUseExistingUser() throws Exception {
        String email = "existing@example.com";
        String name = "Existing User";
        String generatedAccessToken = "existing-access-token";

        User existingUser = User.builder()
                .email(email)
                .username(name)
                .password("")
                .role(Role.USER)
                .enabled(true)
                .build();

        OAuth2User mockOAuth2User = mock(OAuth2User.class);
        when(mockOAuth2User.getAttribute("email")).thenReturn(email);
        when(mockOAuth2User.getAttribute("name")).thenReturn(name);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(jwtService.generateToken(any(User.class), eq(false))).thenReturn(generatedAccessToken);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(mockOAuth2User, null);

        successHandler.onAuthenticationSuccess(request, response, auth);

        verify(userRepository, never()).save(any());
        verify(refreshTokenRepository).deleteByUser(existingUser);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(jwtService).generateToken(existingUser, false);

        String content = response.getContentAsString();
        assertTrue(content.contains("accessToken"));
        assertEquals("application/json", response.getContentType());
    }
}
