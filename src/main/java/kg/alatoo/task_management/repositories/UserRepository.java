package kg.alatoo.task_management.repositories;

import kg.alatoo.task_management.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
