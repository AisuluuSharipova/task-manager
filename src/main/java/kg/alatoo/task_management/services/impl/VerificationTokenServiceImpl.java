package kg.alatoo.task_management.services.impl;

import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.entities.VerificationToken;
import kg.alatoo.task_management.repositories.VerificationTokenRepository;
import kg.alatoo.task_management.services.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;

    @Override
    public VerificationToken generateToken(User user) {
        VerificationToken token = VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusSeconds(86400))
                .used(false)
                .build();
        return tokenRepository.save(token);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void markTokenAsUsed(VerificationToken token) {
        token.setUsed(true);
        tokenRepository.save(token);
    }
}
