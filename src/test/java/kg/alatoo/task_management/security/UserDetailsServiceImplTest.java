package kg.alatoo.task_management.security;

import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void shouldLoadUserByUsername() {
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        var userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(email)
        );
        verify(userRepository, times(1)).findByEmail(email);
    }
}
