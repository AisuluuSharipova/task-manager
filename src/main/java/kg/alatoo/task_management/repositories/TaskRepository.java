package kg.alatoo.task_management.repositories;

import kg.alatoo.task_management.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
