package kg.alatoo.task_management.services.impl;

import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.entities.VerificationToken;
import kg.alatoo.task_management.repositories.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class VerificationTokenServiceImplTest {

    private VerificationTokenRepository tokenRepository;
    private VerificationTokenServiceImpl service;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(VerificationTokenRepository.class);
        service = new VerificationTokenServiceImpl(tokenRepository);
    }

    @Test
    void generateToken_ShouldCreateAndSaveToken() {
        User user = User.builder().id(1L).email("test@example.com").build();
        ArgumentCaptor<VerificationToken> captor = ArgumentCaptor.forClass(VerificationToken.class);

        when(tokenRepository.save(any(VerificationToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VerificationToken token = service.generateToken(user);

        verify(tokenRepository).save(captor.capture());
        VerificationToken savedToken = captor.getValue();

        assertThat(savedToken.getUser()).isEqualTo(user);
        assertThat(savedToken.getToken()).isNotBlank();
        assertThat(savedToken.isUsed()).isFalse();
        assertThat(savedToken.getExpiryDate()).isAfter(Instant.now());
        assertThat(token).isEqualTo(savedToken);
    }

    @Test
    void findByToken_ShouldReturnTokenIfExists() {
        String tokenValue = UUID.randomUUID().toString();
        VerificationToken token = VerificationToken.builder().token(tokenValue).build();

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        Optional<VerificationToken> result = service.findByToken(tokenValue);

        assertThat(result).isPresent().contains(token);
    }

    @Test
    void markTokenAsUsed_ShouldUpdateUsedField() {
        VerificationToken token = VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .used(false)
                .build();

        service.markTokenAsUsed(token);

        assertThat(token.isUsed()).isTrue();
        verify(tokenRepository).save(token);
    }
}
