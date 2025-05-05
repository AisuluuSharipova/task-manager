package kg.alatoo.task_management.repositories;

import kg.alatoo.task_management.entities.VerificationToken;
import kg.alatoo.task_management.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByToken(String token);
}
