package kg.alatoo.task_management.bootstrap;

import jakarta.annotation.PostConstruct;
import kg.alatoo.task_management.entities.Task;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.enums.Role;
import kg.alatoo.task_management.repositories.TaskRepository;
import kg.alatoo.task_management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Profile("!test")
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

        User admin = User.builder()
                .firstName("Aisuluu")
                .lastName("Sharipova")
                .username("Admin")
                .email("admin@example.com")
                .password("admin123")
                .enabled(true)
                .role(Role.ADMIN)
                .build();

        User aisuluu = User.builder()
                .firstName("Uulkan")
                .lastName("Sharipova")
                .username("UulkanSharipova")
                .email("uulkan@gmail.com")
                .password("uulkan123")
                .enabled(true)
                .role(Role.USER)
                .build();

        User uulkan = User.builder()
                .firstName("Yusuf")
                .lastName("Sharipov")
                .username("YusufSharipov")
                .email("yusuf@gmail.com")
                .password("yusuf123")
                .enabled(true)
                .role(Role.USER)
                .build();

        userRepository.saveAll(List.of(admin, aisuluu, uulkan));

        admin = userRepository.findByEmail("admin@example.com").orElseThrow();
        aisuluu = userRepository.findByEmail("uulkan@gmail.com").orElseThrow();
        uulkan = userRepository.findByEmail("yusuf@gmail.com").orElseThrow();

        System.out.println("Users successfully created!");

        Task task1 = new Task(null, "Implement Login", "Create login functionality", "New", "High", LocalDate.now(), LocalDate.parse("2025-04-10"), admin);
        Task task2 = new Task(null, "Fix UI Bugs", "Resolve UI issues on dashboard", "In Progress", "Medium", LocalDate.now(), LocalDate.parse("2025-04-20"), aisuluu);
        Task task3 = new Task(null, "Write API Documentation", "Document API endpoints", "Done", "Low", LocalDate.now(), LocalDate.parse("2025-03-30"), uulkan);

        taskRepository.saveAll(List.of(task1, task2, task3));

        System.out.println("Tasks successfully created!");
    }
}
