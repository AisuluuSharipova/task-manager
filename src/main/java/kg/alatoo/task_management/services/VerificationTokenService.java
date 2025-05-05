package kg.alatoo.task_management.services;

import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.entities.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    VerificationToken generateToken(User user);

    Optional<VerificationToken> findByToken(String token);

    void markTokenAsUsed(VerificationToken token);
}
