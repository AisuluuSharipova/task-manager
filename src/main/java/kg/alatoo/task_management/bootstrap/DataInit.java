package kg.alatoo.task_management.bootstrap;

import jakarta.annotation.PostConstruct;
import kg.alatoo.task_management.entities.Task;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.repositories.TaskRepository;
import kg.alatoo.task_management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) {
            System.out.println("Database already initialized. Skipping sample data creation.");
            return;
        }

        System.out.println("Initializing database with sample data...");

        User admin = new User(null, "Admin", "Admin", "admin@example.com");
        User aisuluu = new User(null, "Aisuluu", "Sharipova", "aisuluu@gmail.com");
        User uulkan = new User(null, "Uulkan", "Sharipova", "uulkan@gmail.com");

        userRepository.saveAll(List.of(admin, aisuluu, uulkan));

        admin = (User) userRepository.findByEmail("admin@example.com").orElseThrow();
        aisuluu = (User) userRepository.findByEmail("aisuluu@gmail.com").orElseThrow();
        uulkan = (User) userRepository.findByEmail("uulkan@gmail.com").orElseThrow();

        System.out.println("Users successfully created!");

        Task task1 = new Task(null, "Implement Login", "Create login functionality", "New", "High", new Date(System.currentTimeMillis()), Date.valueOf("2025-04-10"), admin);
        Task task2 = new Task(null, "Fix UI Bugs", "Resolve UI issues on dashboard", "In Progress", "Medium", new Date(System.currentTimeMillis()), Date.valueOf("2025-04-20"), aisuluu);
        Task task3 = new Task(null, "Write API Documentation", "Document API endpoints", "Done", "Low", new Date(System.currentTimeMillis()), Date.valueOf("2025-03-30"), uulkan);

        taskRepository.saveAll(List.of(task1, task2, task3));

        System.out.println("Tasks successfully created!");
    }
}
